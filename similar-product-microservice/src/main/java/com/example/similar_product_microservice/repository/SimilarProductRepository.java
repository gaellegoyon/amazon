package com.example.similar_product_microservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.similar_product_microservice.entity.SimilarProduct;

public interface SimilarProductRepository extends JpaRepository<SimilarProduct, UUID> {

    List<SimilarProduct> findByProductId(UUID productId);

    SimilarProduct findByProductIdAndSimilarProductId(UUID productId, UUID similarProductId);

}