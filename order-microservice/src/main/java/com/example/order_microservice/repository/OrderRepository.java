package com.example.order_microservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.order_microservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}