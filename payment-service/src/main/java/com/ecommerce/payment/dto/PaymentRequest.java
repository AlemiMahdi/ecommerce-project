package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Request som klienten skickar när en order ska betalas.
 *
 * Klienten får inte skicka:
 * - userId
 * - amount
 * - status
 * - transactionReference
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    
    @NotNull(message = "Order id is required")
    @Positive(message = "Order is must be greater than 0")
    private Long orderId;

    @NotNull(message = "Payment methosd is required")
    private PaymentMethod paymentMethod;

}
