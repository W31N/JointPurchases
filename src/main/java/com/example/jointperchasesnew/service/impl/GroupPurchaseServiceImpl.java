package com.example.jointperchasesnew.service.impl;

import com.example.jointperchasesnew.controller.GroupPurchaseController;
import com.example.jointperchasesnew.controller.UserOrderController;
import com.example.jointperchasesnew.dto.GroupPurchaseDto;
import com.example.jointperchasesnew.exception.GroupPurchaseAlreadyExistException;
import com.example.jointperchasesnew.exception.GroupPurchaseNotFoundException;
import com.example.jointperchasesnew.model.entity.GroupPurchase;

import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.model.entity.UserOrder;
import com.example.jointperchasesnew.repository.GroupPurchaseRepository;
import com.example.jointperchasesnew.repository.UserOrderRepository;
import com.example.jointperchasesnew.repository.UserRepository;
import com.example.jointperchasesnew.representation.GroupPurchaseRepresentation;
import com.example.jointperchasesnew.service.GroupPurchaseService;
import com.example.jointperchasesnew.service.RabbitMQService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class GroupPurchaseServiceImpl implements GroupPurchaseService {

    private GroupPurchaseRepository groupPurchaseRepository;
    private UserRepository userRepository;
    private UserOrderRepository userOrderRepository;
    private final ModelMapper modelMapper;
    private RabbitMQService rabbitMQService;

    public GroupPurchaseServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GroupPurchaseRepresentation createGroupPurchase(GroupPurchaseDto groupPurchaseDto) {
        GroupPurchase newGroupPurchase = getGroupPurchaseDataForCreate(groupPurchaseDto);
        GroupPurchaseRepresentation groupPurchaseRepresentation = modelMapper.map(newGroupPurchase, GroupPurchaseRepresentation.class);
        addLinks(groupPurchaseRepresentation, groupPurchaseDto.getProductName());

        rabbitMQService.sendGroupPurchaseCreatedMessage(
                newGroupPurchase.getProductName(),
                newGroupPurchase.getProductPrice(),
                newGroupPurchase.getMaxQuantity()
        );

        return groupPurchaseRepresentation;
    }

    @Override
    public GroupPurchase getGroupPurchaseDataForCreate(GroupPurchaseDto groupPurchaseDto) {
        if (groupPurchaseRepository.findByProductName(groupPurchaseDto.getProductName()).isPresent())
            throw new GroupPurchaseAlreadyExistException("Group purchase already exist");
        GroupPurchase newGroupPurchase = modelMapper.map(groupPurchaseDto, GroupPurchase.class);
        newGroupPurchase.setCreated(LocalDateTime.now());
        newGroupPurchase.setUpdated(LocalDateTime.now());
        return groupPurchaseRepository.saveAndFlush(newGroupPurchase);
    }

    @Override
    public GroupPurchaseRepresentation getGroupPurchaseByProductName(String productName) {
        Optional<GroupPurchase> optionalGroupPurchase = groupPurchaseRepository.findByProductName(productName);
        if (optionalGroupPurchase.isEmpty())
            throw new GroupPurchaseNotFoundException("Group purchase not found");
        GroupPurchase groupPurchase = getGroupPurchaseDataByProductName(productName);
        GroupPurchaseRepresentation groupPurchaseRepresentation = modelMapper.map(
                groupPurchase,
                GroupPurchaseRepresentation.class
        );
        addLinks(groupPurchaseRepresentation, productName);
        return groupPurchaseRepresentation;
    }

    @Override
    public GroupPurchase getGroupPurchaseDataByProductName(String productName) {
        Optional<GroupPurchase> optionalGroupPurchase = groupPurchaseRepository.findByProductName(productName);
        if (optionalGroupPurchase.isEmpty())
            throw new GroupPurchaseNotFoundException("Group purchase not found");
        return optionalGroupPurchase.get();
    }

    @Override
    public CollectionModel<GroupPurchaseRepresentation> getAllGroupPurchases() {
        List<GroupPurchase> groupPurchases = groupPurchaseRepository.findAll();

        List<GroupPurchaseRepresentation> groupPurchaseRepresentations = groupPurchases.stream()
                .map(groupPurchase -> {
                    GroupPurchaseRepresentation representation = modelMapper.map(groupPurchase, GroupPurchaseRepresentation.class);
                    addLinks(representation, groupPurchase.getProductName());
                    return representation;
                })
                .toList();

        return CollectionModel.of(groupPurchaseRepresentations);
    }

    @Override
    public Iterable<GroupPurchase> getAllDataGroupPurchases() {
        return groupPurchaseRepository.findAll();
    }

    @Override
    public void cancelGroupPurchase(String productName) {
        GroupPurchase groupPurchase = groupPurchaseRepository.findByProductName(productName)
                .orElseThrow(() -> new GroupPurchaseNotFoundException("Group purchase not found"));

        List<UserOrder> relatedOrders = userOrderRepository.findByGroupPurchaseProductName(productName);

        for (UserOrder order : relatedOrders) {
            User user = order.getUser();
            double refundAmount = order.getTotalPrice();

            user.setBalance(user.getBalance() + refundAmount);
            userRepository.saveAndFlush(user);
        }

        userOrderRepository.deleteAll(relatedOrders);

        groupPurchaseRepository.delete(groupPurchase);

//        rabbitMQService.sendPurchaseCancelledMessage(productName, relatedOrders.size());
    }


    private void addLinks(GroupPurchaseRepresentation representation, String productName) {
        Link selfLink = linkTo(methodOn(GroupPurchaseController.class).getGroupPurchase(productName)).withSelfRel();
        Link ordersLink = linkTo(methodOn(UserOrderController.class).getOrdersForGroupPurchase(productName)).withRel("orders");
        representation.add(selfLink).add(ordersLink);
    }

    @Autowired
    public void setUserOrderRepository(UserOrderRepository userOrderRepository) {
        this.userOrderRepository = userOrderRepository;
    }

    @Autowired
    public void setRabbitMQService(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    @Autowired
    public void setGroupPurchaseRepository(GroupPurchaseRepository groupPurchaseRepository) {
        this.groupPurchaseRepository = groupPurchaseRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

