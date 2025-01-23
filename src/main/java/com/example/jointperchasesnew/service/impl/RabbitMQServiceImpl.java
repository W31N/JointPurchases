package com.example.jointperchasesnew.service.impl;

import com.example.jointperchasesnew.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.purchase.name}")
    private String exchangeName;

    @Value("${rabbitmq.purchase.key}")
    private String routingKey;

    @Override
    public void sendPurchaseClosedMessage(String productName, int totalQuantity) {
        String message = String.format("Group purchase for product '%s' is now closed. Total quantity: %d", productName, totalQuantity);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public void sendGroupPurchaseCreatedMessage(String productName, double productPrice, int maxQuantity) {
        String message = String.format("New group purchase created: '%s' with price: %.2f and max quantity: %d", productName, productPrice, maxQuantity);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public void sendOrderAddedMessage(String username, String productName, int quantity) {
        String message = String.format("Order added by '%s' for product '%s' with quantity: %d", username, productName, quantity);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public void sendPurchaseClosedMessageForUser(String productName, int totalQuantity, String username) {
        String message = String.format(
                "Hello, %s! The group purchase for product '%s' is now closed. Total quantity: %d.",
                username, productName, totalQuantity
        );
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
