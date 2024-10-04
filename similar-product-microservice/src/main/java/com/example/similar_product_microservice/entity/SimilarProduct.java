package com.example.similar_product_microservice.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimilarProduct {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID productId;

    private UUID similarProductId;

    private int occurence;

}
