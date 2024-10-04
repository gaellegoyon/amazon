package com.example.user_microservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.common.event.OrderEvent;
import com.example.user_microservice.service.UserService;

@Service
public class OrderKafkaListener {

    private final UserService userService;

    public OrderKafkaListener(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "amazon-orders", groupId = "user-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEvent orderEvent) {
        System.out.println("Received OrderEvent: " + orderEvent);

        userService.addOrderToUser(orderEvent.getUserId(), orderEvent.getOrderId());

        System.out.println("Order " + orderEvent.getOrderId() + " added to user " + orderEvent.getUserId());
    }
}
