package com.ecommerce.payment.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * De orderuppgifter som payment-service behöver
 * från order-service.
 *
 * Okända JSON-fält ignoreras eftersom payment-service
 * inte behöver hela orderobjektet.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class OrderDetailsResponse {
    
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal totalAmount;
}
