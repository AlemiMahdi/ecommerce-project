package com.ecommerce.order.dto;

import lombok.*;

//Request om order-service skicakr till inventory-service när lager ska reserveras
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveInventoryRequest {
    private Long productId;
    private Integer quantity;
}
