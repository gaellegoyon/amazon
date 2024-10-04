package com.example.order_microservice.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {

    private UUID userId;
    private List<UUID> productIds;
}
