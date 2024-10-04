package com.example.order_microservice.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.common.event.OrderEvent;
import com.example.order_microservice.model.Order;
import com.example.order_microservice.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String TOPIC = "amazon-orders";

    public Order createOrder(UUID userId, List<UUID> productIds) {
        Order order = new Order();
        order.setUserId(userId);
        order.setProductIds(productIds);
        order.setCreationDate(ZonedDateTime.now());

        Order savedOrder = orderRepository.save(order);

        publishOrderEvent(savedOrder);

        return savedOrder;
    }

    private void publishOrderEvent(Order order) {
        OrderEvent orderEvent = new OrderEvent(order.getId(), order.getUserId(), order.getProductIds());
        kafkaTemplate.send(TOPIC, orderEvent);
    }

    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    public void deleteAll() {
        orderRepository.deleteAll();
    }

}
