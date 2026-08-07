package com.ecommerce.payment.exception;

//Kastas om en order redan har en slutför betalning
public class PaymentAlreadyCompletedException extends RuntimeException{
    public PaymentAlreadyCompletedException(Long orderId){
        super("Order already has a compeleted payment. Order id: " + orderId);
    }
}
