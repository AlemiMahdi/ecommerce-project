package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * DTO som används när reserverat lager ska släppas tillbaka.
 *
 * Exempel:
 * Om en order avbryts kan reserverat lager bli tillgängligt igen.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseInventoryRequest {
    
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
}
