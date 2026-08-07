package com.ecommerce.payment.entity;

//Möjliga statusar för en betalning
public enum PaymentStatus {
    
    //Betalningen har skapats men annu inte behandlats
    PENDING,

    //Betalningen lyckades
    COMPLETED,

    //Betalningen misslyckades
    FAILED,

    //Betalningen har återbetalats
    REFUNDED
}
