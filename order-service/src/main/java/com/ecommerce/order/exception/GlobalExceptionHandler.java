package com.ecommerce.order.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotfound(OrderNotFoundException exception){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 404,
                        "error", "NOT_FOUND",
                        "message", exception.getMessage()
                ));
    }

    public ResponseEntity<?> handleGenericException(Exception exception){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "staus", 500,
                        "error", "INTERNAL_SERVER_ERROR",
                        "message", "An unexpected error occurred"
                ));
    }
    
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<?> handleInvlidOrderStatus(
        InvalidOrderStatusException exception
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "staus", 400,
                        "error", "BAD_REQEUEST",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<?> HandleProductNotAvailable(
        ProductNotAvailableException exception
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "staus", 400,
                        "error", "BAD_REQUEST",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(ProductserviceException.class)
    public ResponseEntity<?> handleProductServiceException(
        ProductserviceException exception
    ){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "staus", 503,
                        "error", "SERVICE_UNAVAILABLE",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(InventoryOperationException.class)
    public ResponseEntity<?> handleInventoryOperationException(
        InventoryOperationException exception
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "CONFLICT",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(InventoryServiceException.class)
    public ResponseEntity<?> handleInventoryServiceException(
        InventoryServiceException exception
    ){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "erro", "SERVICE_UNAVAILABLE",
                        "message", exception.getMessage()
                ));
    }
}
