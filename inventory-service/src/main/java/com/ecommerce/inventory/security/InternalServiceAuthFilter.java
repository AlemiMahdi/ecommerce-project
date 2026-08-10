package com.ecommerce.inventory.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;

//Skyddar endpoints som endast får användas av andra mikrotjänster
@Component
public class InternalServiceAuthFilter extends OncePerRequestFilter{

    private final String internalServiceKey;
    public InternalServiceAuthFilter(
        @Value("${internal.service-key}") String internalServiceKey
    ){
        this.internalServiceKey = internalServiceKey;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    )throws ServletException, IOException {

        String path = request.getRequestURI();

        //Vanliga inventory-endpiitns påverkas inte
        if (!isInternalInventoryEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        String providedKey = request.getHeader("X-Internal-Service-Key");

        //Saknad eller felaktig key
        if (!isValidKey(providedKey)) {
            response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isInternalInventoryEndpoint(String path) {

        return "/api/v1/inventory/reserve".equals(path)
                || "/api/v1/inventory/release".equals(path)
                || "/api/v1/inventory/confirm".equals(path);
    }

    private boolean isValidKey(String providedKey){
        if (providedKey == null) {
            return false;
        }
        
        return MessageDigest.isEqual(
            internalServiceKey.getBytes(StandardCharsets.UTF_8), 
            providedKey.getBytes(StandardCharsets.UTF_8)
        );
    }
    
    


}
