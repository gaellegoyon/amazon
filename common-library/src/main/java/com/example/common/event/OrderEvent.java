package com.example.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    @JsonProperty("orderId")
    private UUID orderId;

    @JsonProperty("userId")
    private UUID userId;

    @JsonProperty("productIds")
    private List<UUID> productIds;
}
