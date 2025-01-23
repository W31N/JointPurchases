package com.example.jointperchasesnew.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.purchase.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.purchase.name}")
    private String exchangeName;

    @Value("${rabbitmq.purchase.key}")
    private String routingKey;

    @Bean
    public Queue purchaseQueue() {
        return new Queue(queueName, false); // Durable queue
    }

    @Bean
    public Exchange purchaseExchange() {
        return new TopicExchange(exchangeName, false, false); // Durable exchange
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(purchaseQueue()).to(purchaseExchange()).with(routingKey).noargs();
    }
}
