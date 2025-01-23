package com.example.jointperchasesnew.grpc;

import com.example.jointperchasesnew.*;
import com.example.jointperchasesnew.dto.GroupPurchaseDto;
import com.example.jointperchasesnew.dto.UserOrderDto;
import com.example.jointperchasesnew.dto.UserReplenishmentDto;
import com.example.jointperchasesnew.model.entity.GroupPurchase;
import com.example.jointperchasesnew.service.GroupPurchaseService;
import com.example.jointperchasesnew.service.UserOrderService;
import com.example.jointperchasesnew.service.UserService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class GroupPurchaseGrpcService extends GroupPurchaseServiceGrpc.GroupPurchaseServiceImplBase {

    private  UserService userService;
    private  UserOrderService userOrderService;
    private  GroupPurchaseService groupPurchaseService;

    @Override
    public void deposit(UserDepositRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println("Received deposit request: " + request);
        try {
            UserReplenishmentDto dto = new UserReplenishmentDto();
            dto.setUsername(request.getUsername());
            dto.setDepositAmount(request.getAmount());

            var user = userService.getUserDataForDeposit(dto);

            UserResponse response = UserResponse.newBuilder()
                    .setUsername(user.getUsername())
                    .setBalance(user.getBalance())
                    .build();

            System.out.println("Deposit successful for user: " + response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("Error processing deposit: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void addUserOrder(AddUserOrderRequest request, StreamObserver<UserOrderResponse> responseObserver) {
        System.out.println("Received add order request: " + request);
        try {
            UserOrderDto dto = new UserOrderDto();
            dto.setUsername(request.getUsername());
            dto.setProductName(request.getProductName());
            dto.setQuantity(request.getQuantity());

            var order = userOrderService.getUserOrderDataForAdd(dto);

            UserOrderResponse response = UserOrderResponse.newBuilder()
                    .setUsername(order.getUser().getUsername())
                    .setProductName(order.getGroupPurchase().getProductName())
                    .setQuantity(order.getQuantity())
                    .setTotalPrice(order.getTotalPrice())
                    .build();

            System.out.println("Order added successfully: " + response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("Error adding order: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void createGroupPurchase(CreateGroupPurchaseRequest request, StreamObserver<GroupPurchaseResponse> responseObserver) {
        System.out.println("Received create group purchase request: " + request);
        try {
            GroupPurchaseDto groupPurchaseDto = new GroupPurchaseDto();
            groupPurchaseDto.setProductName(request.getProductName());
            groupPurchaseDto.setProductPrice(request.getProductPrice());
            groupPurchaseDto.setDescription(request.getDescription());
            groupPurchaseDto.setDeadline(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(request.getDeadline().getSeconds(), request.getDeadline().getNanos()),
                    ZoneId.systemDefault()
            ));
            groupPurchaseDto.setMaxQuantity(request.getMaxQuantity());

            GroupPurchase groupPurchase = groupPurchaseService.getGroupPurchaseDataForCreate(groupPurchaseDto);

            GroupPurchaseResponse response = GroupPurchaseResponse.newBuilder()
                    .setProductName(groupPurchase.getProductName())
                    .setProductPrice(groupPurchase.getProductPrice())
                    .setDescription(groupPurchase.getDescription())
                    .setDeadline(com.google.protobuf.Timestamp.newBuilder()
                            .setSeconds(groupPurchase.getDeadline().atZone(ZoneId.systemDefault()).toEpochSecond())
                            .setNanos(groupPurchase.getDeadline().getNano())
                            .build())
                    .setTotalQuantity(groupPurchase.getTotalQuantity())
                    .setMaxQuantity(groupPurchase.getMaxQuantity())
                    .setStatus(GroupPurchaseResponse.PurchaseStatus.valueOf(groupPurchase.getStatus().name()))
                    .build();

            System.out.println("Group purchase created successfully: " + response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("Error creating group purchase: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserOrderService(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    @Autowired
    public void setGroupPurchaseService(GroupPurchaseService groupPurchaseService) {
        this.groupPurchaseService = groupPurchaseService;
    }
}

