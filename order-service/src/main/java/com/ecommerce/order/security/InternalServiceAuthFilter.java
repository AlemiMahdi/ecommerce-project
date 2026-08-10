package com.ecommerce.order.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//Skyddar order-service internal endpoints
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
    ) throws ServletException, IOException{
        String path  = request.getRequestURI();

        //Bara internal/orders/** skyddas av denna filter */
        if (!path.startsWith("/internal/orders/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedKey = request.getHeader("X-Internal-Service-Key");
        if (!isValidKey(providedKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
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
