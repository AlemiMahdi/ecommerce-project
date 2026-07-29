package com.ecommerce.order.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

//Lagerinformation som order-service tar emot från inventory-service
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Integer quantityAvailable;
    private Integer quantityReserved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
