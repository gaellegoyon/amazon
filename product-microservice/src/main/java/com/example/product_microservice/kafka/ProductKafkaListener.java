package com.example.product_microservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.common.event.OrderEvent;
import com.example.product_microservice.service.ProductService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class ProductKafkaListener {

    @Autowired
    private ProductService productService;

    @Transactional
    @KafkaListener(topics = "amazon-orders", groupId = "product-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEvent orderEvent) {
        System.out.println("Received OrderEvent: " + orderEvent);

        List<UUID> productIds = orderEvent.getProductIds();
        UUID orderId = orderEvent.getOrderId();
        productService.addOrderToProducts(productIds, orderId);
    }
}
