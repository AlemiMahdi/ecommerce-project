package com.ecommerce.payment.service;
//Affärslogik för betalningar

import java.util.List;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;

public interface PaymentService {
    
    //Skapar och behandlar ett betalningsförsök
    PaymentResponse processPayment(Long userId, PaymentRequest request);

    //Hämtar en specifik betalning som tillhör användare
    PaymentResponse getPaymentById(Long paymentId, Long userId);

    //Hämtar den inloggade användares betalningar
    List<PaymentResponse> getMyPayments(Long userId);

}
