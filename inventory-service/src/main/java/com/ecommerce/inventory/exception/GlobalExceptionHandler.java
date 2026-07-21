package com.ecommerce.inventory.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

//Globl felhantering för inventory-service
public class GlobalExceptionHandler {
    
    //Hanterar när inventory inte hittas
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<?> handleInventoryNotFound(
        InventoryNotFoundException exception,
        HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 404,
                    "error", "NOT_FOUND",
                    "message", exception.getMessage(),
                    "path", request.getRequestURI()

                ));
    }

    //Hanterar när inventory redan finns för en produkt
    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<?> handleInventoryAlreadyExists(
        InventoryAlreadyExistsException exception,
        HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "CONFLICT",
                        "message", exception.getMessage(),
                        "path", request.getRequestURI()
                ));
    }

    //Hanterar när lagret inte räcker
    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<?> handleInssuficientInventory(
        InsufficientInventoryException exception,
        HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "staus", 400,
                        "error", "BAD_REQUEST",
                        "message", exception.getMessage(),
                        "path", request.getRequestURI()
                ));
    }

    //Hanterar validation-fel från @Valid
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
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of(
                "timestamp", LocalDateTime.now(),
                "staus", 400,
                "error", "BAD_REQUEST",
                "message", message,
                "path", request.getRequestURI()
            ));
    }
    
    //Hanterar oväntade fel
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
        Exception exception,
        HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 500,
                    "error", "INTERNAL_SERVER_ERROR",
                    "path", request.getRequestURI()
                ));
    }



}
