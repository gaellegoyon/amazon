package com.example.similar_product_microservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.common.event.OrderEvent;
import com.example.similar_product_microservice.service.SimilarProductService;

@Component
public class OrderKafkaListener {

    @Autowired
    private SimilarProductService similarProductService;

    @KafkaListener(topics = "amazon-orders", groupId = "similar-product-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEvent orderEvent) {

        System.out.println("Received order event: " + orderEvent);

        similarProductService.updateSimilarProducts(orderEvent.getProductIds());
    }
}
