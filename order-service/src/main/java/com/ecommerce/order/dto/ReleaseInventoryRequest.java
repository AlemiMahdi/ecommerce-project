package com.ecommerce.order.dto;

import lombok.*;

//Request om order-service skickar till inventory-service när reserverat lager ska släppas tillbaka
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseInventoryRequest {
    
    private Long productId;
    private Integer quantity;
}
