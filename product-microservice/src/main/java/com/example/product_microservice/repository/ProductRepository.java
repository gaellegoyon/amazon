package com.example.product_microservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product_microservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
