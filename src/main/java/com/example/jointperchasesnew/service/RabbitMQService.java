package com.example.jointperchasesnew.service;

public interface RabbitMQService {
    void sendPurchaseClosedMessage(String productName, int totalQuantity);
    void sendGroupPurchaseCreatedMessage(String productName, double productPrice, int maxQuantity);
    void sendOrderAddedMessage(String username, String productName, int quantity);
    void sendPurchaseClosedMessageForUser(String productName, int totalQuantity, String username);
}
