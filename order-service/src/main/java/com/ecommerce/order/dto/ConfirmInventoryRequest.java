package com.ecommerce.order.dto;

import lombok.*;

//Request som skicakr till inventory-serive när reserverat lager ska bekräftas som sålt
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmInventoryRequest {

    private Long productId;
    private Integer quantity;
    
}
