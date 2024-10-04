package com.example.product_microservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product_microservice.model.Product;
import com.example.product_microservice.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void addOrderToProducts(List<UUID> productIds, UUID orderId) {
        productIds.forEach(productId -> {
            Product product = findById(productId);
            product.getOrders().add(orderId);
            productRepository.save(product);
        });
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

}
