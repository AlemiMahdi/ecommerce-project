package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.PaymentStatus;

import lombok.*;

//Betalning som skickar till order-service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentResultRequest {
    private PaymentStatus paymentStatus;
}
