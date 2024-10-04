package com.example.product_microservice.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    private Double price;
    private String description;

    @ElementCollection
    private List<UUID> orders = new ArrayList<>();
}
