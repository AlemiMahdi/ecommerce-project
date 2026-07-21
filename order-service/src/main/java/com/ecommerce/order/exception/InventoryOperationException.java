package com.ecommerce.order.exception;

/**
 * Kastas när inventory-service svarar att en lageroperation
 * inte kan genomföras.
 *
 * Exempel:
 * - lagret räcker inte
 * - inventory saknas för produkten
 * - mer lager släpps än vad som är reserverat
 */
public class InventoryOperationException extends RuntimeException{
    public InventoryOperationException(String message){
        super(message);
    }
}
