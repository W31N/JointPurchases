package com.example.jointperchasesnew.service.impl;


import com.example.jointperchasesnew.controller.GroupPurchaseController;
import com.example.jointperchasesnew.controller.UserController;
import com.example.jointperchasesnew.dto.OrderCancellationDto;
import com.example.jointperchasesnew.dto.UserOrderDto;
import com.example.jointperchasesnew.exception.GroupPurchaseNotFoundException;
import com.example.jointperchasesnew.exception.OrderNotFoundException;
import com.example.jointperchasesnew.exception.UserNotFoundException;
import com.example.jointperchasesnew.model.entity.GroupPurchase;
import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.model.entity.UserOrder;
import com.example.jointperchasesnew.model.enums.PurchaseStatus;
import com.example.jointperchasesnew.repository.GroupPurchaseRepository;
import com.example.jointperchasesnew.repository.UserOrderRepository;
import com.example.jointperchasesnew.repository.UserRepository;
import com.example.jointperchasesnew.representation.UserOrderRepresentation;
import com.example.jointperchasesnew.service.RabbitMQService;
import com.example.jointperchasesnew.service.UserOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserOrderServiceImpl implements UserOrderService {
    private UserRepository userRepository;
    private GroupPurchaseRepository groupPurchaseRepository;
    private UserOrderRepository userOrderRepository;
    private final ModelMapper modelMapper;
    private RabbitMQService rabbitMQService;

    public UserOrderServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserOrderRepresentation addUserOrder(UserOrderDto userOrderDto) {
        UserOrder userOrder = getUserOrderDataForAdd(userOrderDto);
        UserOrderRepresentation userOrderRepresentation = modelMapper.map(userOrder, UserOrderRepresentation.class);
        return userOrderRepresentation;
    }

    @Override
    public UserOrder getUserOrderDataForAdd(UserOrderDto userOrderDto) {
        User user = userRepository.findByUsername(userOrderDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        GroupPurchase groupPurchase = groupPurchaseRepository.findByProductName(userOrderDto.getProductName())
                .orElseThrow(() -> new GroupPurchaseNotFoundException("Групповая закупка не найдена"));

        if (groupPurchase.getStatus() != PurchaseStatus.OPEN) {
            throw new IllegalStateException("Групповая закупка закрыта и не принимает заказы");
        }

        if (groupPurchase.getTotalQuantity() + userOrderDto.getQuantity() > groupPurchase.getMaxQuantity()) {
            throw new IllegalArgumentException("Превышено максимальное количество товара для закупки");
        }

        Optional<UserOrder> existingOrder = userOrderRepository.findByUser_UsernameAndGroupPurchase_ProductName(
                user.getUsername(), groupPurchase.getProductName()
        );

        if (existingOrder.isPresent()) {
            UserOrder order = existingOrder.get();

            double additionalCost = userOrderDto.getQuantity() * groupPurchase.getProductPrice();
            if (user.getBalance() < additionalCost) {
                throw new IllegalArgumentException("Недостаточно средств на балансе для обновления заказа");
            }

            order.setQuantity(order.getQuantity() + userOrderDto.getQuantity());
            order.setTotalPrice(order.getTotalPrice() + additionalCost);
            order.setUpdated(LocalDateTime.now());

            userOrderRepository.saveAndFlush(order);

            groupPurchase.setTotalQuantity(groupPurchase.getTotalQuantity() + userOrderDto.getQuantity());
            if (groupPurchase.getTotalQuantity() == groupPurchase.getMaxQuantity()) {
                closeGroupPurchase(groupPurchase);
            } else {
                groupPurchaseRepository.saveAndFlush(groupPurchase);
            }

            user.setBalance(user.getBalance() - additionalCost);
            userRepository.saveAndFlush(user);

            rabbitMQService.sendOrderAddedMessage(user.getUsername(), groupPurchase.getProductName(), userOrderDto.getQuantity());

            return order;

        } else {
            double totalCost = userOrderDto.getQuantity() * groupPurchase.getProductPrice();
            if (user.getBalance() < totalCost) {
                throw new IllegalArgumentException("Недостаточно средств на балансе для создания нового заказа");
            }

            UserOrder newOrder = new UserOrder();
            newOrder.setUser(user);
            newOrder.setGroupPurchase(groupPurchase);
            newOrder.setQuantity(userOrderDto.getQuantity());
            newOrder.setTotalPrice(totalCost);
            newOrder.setCreated(LocalDateTime.now());
            newOrder.setUpdated(LocalDateTime.now());

            newOrder = userOrderRepository.saveAndFlush(newOrder);

            groupPurchase.setTotalQuantity(groupPurchase.getTotalQuantity() + userOrderDto.getQuantity());
            if (groupPurchase.getTotalQuantity() == groupPurchase.getMaxQuantity()) {
                closeGroupPurchase(groupPurchase);
            } else {
                groupPurchaseRepository.saveAndFlush(groupPurchase);
            }

            user.setBalance(user.getBalance() - totalCost);
            userRepository.saveAndFlush(user);

            rabbitMQService.sendOrderAddedMessage(user.getUsername(), groupPurchase.getProductName(), userOrderDto.getQuantity());

            return newOrder;
        }
    }

    private void closeGroupPurchase(GroupPurchase groupPurchase) {
        groupPurchase.setStatus(PurchaseStatus.COMPLETED);
        groupPurchase.setUpdated(LocalDateTime.now());
        groupPurchaseRepository.saveAndFlush(groupPurchase);

        rabbitMQService.sendPurchaseClosedMessage(
                groupPurchase.getProductName(),
                groupPurchase.getTotalQuantity()
        );

        List<User> users = userOrderRepository.findUsersByGroupPurchaseId(groupPurchase.getId());

        users.forEach(user -> {
            rabbitMQService.sendPurchaseClosedMessageForUser(
                    groupPurchase.getProductName(),
                    groupPurchase.getTotalQuantity(),
                    user.getUsername()
            );
        });
    }

    @Override
    public CollectionModel<UserOrderRepresentation> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с именем '" + username + "' не найден"));

        List<UserOrder> userOrders = userOrderRepository.findByUserUsername(user.getUsername());

        List<UserOrderRepresentation> representations = userOrders.stream()
                .map(this::toRepresentation)
                .collect(Collectors.toList());

        return CollectionModel.of(representations);
    }

    @Override
    public Iterable<UserOrder> getDataUserOrders(String username) {
        return userOrderRepository.findByUserUsername(username);
    }

    @Override
    public CollectionModel<UserOrderRepresentation> getOrdersForGroupPurchase(String productName) {
        GroupPurchase groupPurchase = groupPurchaseRepository.findByProductName(productName)
                .orElseThrow(() -> new GroupPurchaseNotFoundException("Групповая закупка для продукта '" + productName + "' не найдена"));

        List<UserOrder> orders = userOrderRepository.findByGroupPurchaseProductName(groupPurchase.getProductName());

        List<UserOrderRepresentation> representations = orders.stream()
                .map(this::toRepresentation)
                .collect(Collectors.toList());

        return CollectionModel.of(representations);
    }

    @Override
    public Iterable<UserOrder> getDataOrdersForGroupPurchase(String productName) {
        return userOrderRepository.findByGroupPurchaseProductName(productName);
    }

    @Override
    public CollectionModel<UserOrderRepresentation> getAllUserOrders() {
        List<UserOrder> userOrders = userOrderRepository.findAll();

        List<UserOrderRepresentation> userOrderRepresentations = userOrders.stream()
                .map(userOrder -> {
                    UserOrderRepresentation representation = modelMapper.map(userOrder, UserOrderRepresentation.class);
                    return addLinks(representation);
                })
                .toList();

        return CollectionModel.of(userOrderRepresentations);
    }

    @Override
    public Iterable<UserOrder> getAllDataUserOrders() {
        return userOrderRepository.findAll();
    }

    @Override
    public void cancelOrder(OrderCancellationDto orderCancellationDto) {
        User user = userRepository.findByUsername(orderCancellationDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + orderCancellationDto.getUsername()));

        GroupPurchase groupPurchase = groupPurchaseRepository.findByProductName(orderCancellationDto.getProductName())
                .orElseThrow(() -> new GroupPurchaseNotFoundException("Групповая закупка не найдена: " + orderCancellationDto.getProductName()));

        UserOrder order = userOrderRepository.findByUser_UsernameAndGroupPurchase_ProductName(
                orderCancellationDto.getUsername(),
                orderCancellationDto.getProductName()
        ).orElseThrow(() -> new OrderNotFoundException("Заказ не найден для пользователя: "
                + orderCancellationDto.getUsername() + " и продукта: " + orderCancellationDto.getProductName()));

        double refundAmount = order.getTotalPrice();
        user.setBalance(user.getBalance() + refundAmount);
        userRepository.saveAndFlush(user);

        groupPurchase.setTotalQuantity(groupPurchase.getTotalQuantity() - order.getQuantity());
        groupPurchaseRepository.saveAndFlush(groupPurchase);


        userOrderRepository.delete(order);

//        rabbitMQService.sendOrderCancelledMessage(
//                orderCancellationDto.getUsername(),
//                orderCancellationDto.getProductName(),
//                refundAmount
//        );
    }


    private UserOrderRepresentation toRepresentation(UserOrder userOrder) {
        UserOrderRepresentation representation = modelMapper.map(userOrder, UserOrderRepresentation.class);
        return addLinks(representation);
    }

    private UserOrderRepresentation addLinks(UserOrderRepresentation representation) {
        Link usersLink = linkTo(methodOn(UserController.class).getUser(representation.getUsername())).withRel("users");
        Link groupPurchasesLink = linkTo(methodOn(GroupPurchaseController.class).getGroupPurchase(representation.getProductName())).withRel("groupPurchases");
        representation.add(usersLink).add(groupPurchasesLink);
        return representation;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setGroupPurchaseRepository(GroupPurchaseRepository groupPurchaseRepository) {
        this.groupPurchaseRepository = groupPurchaseRepository;
    }

    @Autowired
    public void setUserOrderRepository(UserOrderRepository userOrderRepository) {
        this.userOrderRepository = userOrderRepository;
    }

    @Autowired
    public void setRabbitMQService(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }
}