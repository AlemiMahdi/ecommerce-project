package com.ecommerce.payment.exception;
//Kastas när betalning inte hittas
public class PaymentNotFoundException extends RuntimeException{
    
    public PaymentNotFoundException(Long paymentId){
        super("Payment not found with id: " + paymentId);
    }
}
