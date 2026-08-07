package com.ecommerce.payment.exception;
/**
 * Kastas när en order inte kan betalas.
 *
 * Exempel:
 * - ordern hittas inte
 * - ordern tillhör inte användaren
 * - ordern har fel status
 * - orderbeloppet är ogiltigt
 */
public class OrderNotPayableException extends RuntimeException{
    public OrderNotPayableException(String message){
        super(message);
    }
}
