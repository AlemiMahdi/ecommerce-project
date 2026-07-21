package com.ecommerce.inventory.exception;

//Kastas när man försöker skapa lager för en product som redan har lager
public class InventoryAlreadyExistsException extends RuntimeException{

    public InventoryAlreadyExistsException (Long productId){
        super("Inventory already exists for product id: " + productId);
    }
}
