package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

//Request som payment-service skickar till order-service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultRequest {
    
    @NotNull(message = "Payment status is required")
    private PaymentResultStatus paymentStatus;
}
