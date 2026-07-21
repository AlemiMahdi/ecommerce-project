package com.ecommerce.inventory.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

//DTO som används när man skapar eller uppdaterar lager för en produkt
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {
    
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "Quantity available is required")
    @PositiveOrZero(message = "Quantity avaiable cannot be negative")
    private Integer quantityAvailable;
}
