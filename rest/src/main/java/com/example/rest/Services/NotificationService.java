package com.example.rest.Services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final AmqpTemplate amqpTemplate;
    private final DirectExchange directExchange;

    @Autowired
    public NotificationService(AmqpTemplate amqpTemplate, DirectExchange directExchange) {
        this.amqpTemplate = amqpTemplate;
        this.directExchange = directExchange;
    }

    public void sendNotification(String message) {
        amqpTemplate.convertAndSend(directExchange.getName(),"firstStepRoute", message);
        System.out.println("Sent message: " + message);
    }
}
