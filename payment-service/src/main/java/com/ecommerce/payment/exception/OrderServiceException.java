package com.ecommerce.payment.exception;
//Kastas när payment-service kan inte kommunicera korrekt med order-service
public class OrderServiceException extends RuntimeException{
    public OrderServiceException(String message){
        super(message);
    }
}
