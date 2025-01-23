package com.example.jointperchasesnew.service;

import com.example.jointperchasesnew.dto.OrderCancellationDto;
import com.example.jointperchasesnew.dto.UserOrderDto;
import com.example.jointperchasesnew.dto.UserRegistrationDto;
import com.example.jointperchasesnew.model.entity.User;
import com.example.jointperchasesnew.model.entity.UserOrder;
import com.example.jointperchasesnew.representation.UserOrderRepresentation;
import org.springframework.hateoas.CollectionModel;

public interface UserOrderService {
    public UserOrderRepresentation addUserOrder(UserOrderDto userOrderDto);
    public UserOrder getUserOrderDataForAdd(UserOrderDto userOrderDto);
    public CollectionModel<UserOrderRepresentation> getUserOrders(String username);
    public Iterable<UserOrder> getDataUserOrders(String username);
    public CollectionModel<UserOrderRepresentation> getOrdersForGroupPurchase(String productName);
    public Iterable<UserOrder> getDataOrdersForGroupPurchase(String productName);
    public CollectionModel<UserOrderRepresentation> getAllUserOrders ();
    public Iterable<UserOrder> getAllDataUserOrders();
    public void cancelOrder(OrderCancellationDto orderCancellationDto);
}
