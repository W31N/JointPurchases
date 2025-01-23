package com.example.jointperchasesnew.controller;


import com.example.jointperchasesnew.dto.OrderCancellationDto;
import com.example.jointperchasesnew.dto.UserOrderDto;
import com.example.jointperchasesnew.representation.UserOrderRepresentation;
import com.example.jointperchasesnew.representation.UserRepresentation;
import com.example.jointperchasesnew.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserOrderController {

    private UserOrderService userOrderService;

    // Добавление нового заказа
    @PostMapping("/buy")
    public ResponseEntity<UserOrderRepresentation> addUserOrder(@RequestBody UserOrderDto userOrderDto) {
        UserOrderRepresentation representation = userOrderService.addUserOrder(userOrderDto);
        return ResponseEntity.ok(representation);
    }

    @GetMapping("/ordersUser/{username}")
    public ResponseEntity<CollectionModel<UserOrderRepresentation>> getUserOrders(@PathVariable String username) {
        CollectionModel<UserOrderRepresentation> orders = userOrderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/ordersUser")
    public ResponseEntity<CollectionModel<UserOrderRepresentation>> getAllUserOrders() {
        CollectionModel<UserOrderRepresentation> userOrders = userOrderService.getAllUserOrders();
        return ResponseEntity.ok(userOrders);
    }

    @GetMapping("/ordersProduct/{productName}")
    public ResponseEntity<CollectionModel<UserOrderRepresentation>> getOrdersForGroupPurchase(@PathVariable String productName) {
        CollectionModel<UserOrderRepresentation> orders = userOrderService.getOrdersForGroupPurchase(productName);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/cancelOrder")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderCancellationDto orderCancellationDto) {
        userOrderService.cancelOrder(orderCancellationDto);
        return ResponseEntity.ok(String.format("Order for product '%s' by user '%s' has been cancelled successfully.",
                orderCancellationDto.getProductName(), orderCancellationDto.getUsername()));
    }


    @Autowired
    public void setUserOrderService(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }
}
