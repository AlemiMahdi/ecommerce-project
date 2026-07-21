package com.ecommerce.inventory.exception;

//Kastas när det inte finns tillräckligt med lager att reservera eller släppa
public class InsufficientInventoryException extends RuntimeException{
    
    public InsufficientInventoryException(String message){
        super(message);
    }
}
