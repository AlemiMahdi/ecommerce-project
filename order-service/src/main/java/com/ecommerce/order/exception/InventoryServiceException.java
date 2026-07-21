package com.ecommerce.order.exception;

//Kastas när order-service kan inte kommunicear med inventory-service
public class InventoryServiceException extends RuntimeException{
    public InventoryServiceException(String message){
        super(message);
    }    
}
