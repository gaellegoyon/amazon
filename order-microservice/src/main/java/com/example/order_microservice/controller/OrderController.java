package com.example.order_microservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_microservice.dto.OrderRequest;
import com.example.order_microservice.exceptions.BadRequestException;
import com.example.order_microservice.exceptions.NotFoundException;
import com.example.order_microservice.model.Order;
import com.example.order_microservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        if (orderRequest.getProductIds() == null || orderRequest.getProductIds().isEmpty()) {
            throw new BadRequestException("La liste des IDs de produits ne peut pas être vide.");
        }
        Order order = orderService.createOrder(orderRequest.getUserId(), orderRequest.getProductIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<Iterable<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Order order = orderService.findById(id);

        if (order == null) {
            throw new NotFoundException("Commande non trouvée pour l'ID: " + id);
        }

        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable UUID id) {
        Order order = orderService.findById(id);

        if (order == null) {
            throw new NotFoundException("Impossible de supprimer. Commande non trouvée pour l'ID: " + id);
        }

        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllOrders() {
        orderService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
