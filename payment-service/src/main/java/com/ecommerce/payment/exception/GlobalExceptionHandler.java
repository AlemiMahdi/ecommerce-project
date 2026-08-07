package com.ecommerce.payment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

//Global felhantering för payment-service
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    //Betalningen hittades inte eller tillhör inte användaren
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<?> handlePaymentNotFound(
        PaymentNotFoundException exception,
        HttpServletRequest request
    ){
        return buildResponse(
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            request
        );
    }

    //Order har redan en lyckad betalning
    @ExceptionHandler(PaymentAlreadyCompletedException.class)
    public ResponseEntity<?> handlePaymentAlreadyCompleted(
        PaymentAlreadyCompletedException exception,
        HttpServletRequest request
    ){
        return buildResponse(
            HttpStatus.CONFLICT, 
            exception.getMessage(), 
            request
        );
    }

    //Ordern kan inte betalas
    @ExceptionHandler(OrderNotPayableException.class)
    public ResponseEntity<?> handleOrderNotPayable(
        OrderNotPayableException exception,
        HttpServletRequest request
    ){
        return buildResponse(
            HttpStatus.CONFLICT, 
            exception.getMessage(), 
            request
        );
    }

    //Order-service är ej tillgänligt eller returnerar ett oväntat fel
    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<?> handleOrderServiceException(
        OrderServiceException exception,
        HttpServletRequest request
    ){
        return buildResponse(
            HttpStatus.SERVICE_UNAVAILABLE, 
            exception.getMessage(), 
            request
        );

    }

    //Validation fel från @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ){
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");
        return buildResponse(
            HttpStatus.BAD_REQUEST, 
            message, 
            request
        );
    }

    //Hanterar exempelvis en betalningsmetod som inte finns ex "paymentMethod: "CASH"".
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleUnreadableMessage(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ){
        return buildResponse(
            HttpStatus.BAD_REQUEST, 
            "Invalid request body or payment method", 
            request
        );
    }

    //Hanterar oväntade fel
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
        Exception exception,
        HttpServletRequest request
    ){
        return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "An unexpected error occured", 
            request
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
        HttpStatus status,
        String message,
        HttpServletRequest request
    ){
        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", status.value(),
            "error", status.name(),
            "message", message,
            "path", request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}
