package com.ecommerce.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//Rest-controller för betalningar
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;

    //Skapar och behandlar en betalning : POST /api/v1/payments
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
        @RequestHeader("X-User-Id") Long userId,
        @Valid @RequestBody PaymentRequest request
    ){
        PaymentResponse response = paymentService.processPayment(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Hämtar en inloggade användarens betalningar : GET /api/v1/payments/my-payments
    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
        @RequestHeader("X-User-Id") Long userId
    ){
        List<PaymentResponse> responses = paymentService.getMyPayments(userId);
        return ResponseEntity.ok(responses);
    }

    //Hämtar en specifik betalning som tillhör användaren : GET /api/v1/payments/{paymentId}
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
        @PathVariable Long paymentId,
        @RequestHeader("X-User-Id") Long userId
    ){
        PaymentResponse response = paymentService.getPaymentById(paymentId, userId);
        return ResponseEntity.ok(response);
    }
}
