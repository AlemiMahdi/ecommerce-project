package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

import lombok.*;

//DTO som returneras från inventory-service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {
    
    private Long id;
    private Long productId;
    private Integer quantityAvailable;
    private Integer quantityReserved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
