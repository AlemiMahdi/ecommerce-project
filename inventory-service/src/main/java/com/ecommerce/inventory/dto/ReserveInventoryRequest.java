package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

//DTO som används när en order vill reservera lager
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveInventoryRequest {
    
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
}
