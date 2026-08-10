package com.ecommerce.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.PaymentResultRequest;
import com.ecommerce.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//Intern controller för kommunkation mellan mikrotjänster
//Denna endpoint ska inte routas genom API Gateway
@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
public class InternalOrderController {
    
    private final OrderService orderService;

    /**
     * Tar emot betalningsresultat från payment-service.
     *
     * PATCH /internal/orders/{orderId}/payment-result
     */
    @PatchMapping("/{orderId}/payment-result")
    public ResponseEntity<OrderResponse> handlePaymentResult(
        @PathVariable Long orderId,
        @RequestHeader("X-User-Id") Long userId,
        @Valid @RequestBody PaymentResultRequest request
    ){
        OrderResponse response = orderService.handlePaymentResult(orderId, userId, request);
        return ResponseEntity.ok(response);
    }


    //Intern endpoint för att hämta en order, Avnänds exempelvis av payment-service
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
        @PathVariable Long orderId,
        @RequestHeader("X-User-Id") Long userId
    ){
        OrderResponse response = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(response);
    }

}
