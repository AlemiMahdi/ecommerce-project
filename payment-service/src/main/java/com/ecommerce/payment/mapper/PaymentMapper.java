package com.ecommerce.payment.mapper;

import java.math.BigDecimal;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.entity.PaymentStatus;

//Konverterar mellan Payment-entityn och payment DTO:er
public class PaymentMapper {
    
    //Hindrar att utility-klassen skapad med new
    private PaymentMapper(){}

    /**
     * Skapar en Payment-entity.
     *
     * userId kommer från gateway-headern.
     * amount kommer från order-service.
     */
    public static Payment toEntity(
        PaymentRequest request,
        Long userId,
        BigDecimal amount
    ){
        return Payment.builder()
                .orderId(request.getOrderId())
                .userId(userId)
                .amount(amount)
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .build();
    }

    //Konverterar Payment till PaymentResponse
    public static PaymentResponse toResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
