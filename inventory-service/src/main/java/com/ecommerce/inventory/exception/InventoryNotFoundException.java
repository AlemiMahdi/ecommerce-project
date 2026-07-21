package com.ecommerce.inventory.exception;

//Kastas när lagerinformation för en produkt inte hittas
public class InventoryNotFoundException extends RuntimeException{
    
    public InventoryNotFoundException(Long productId){
        super("Inventory not found for product id: " + productId);
    }

}
