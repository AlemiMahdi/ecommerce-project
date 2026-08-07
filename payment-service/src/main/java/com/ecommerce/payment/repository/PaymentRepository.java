package com.ecommerce.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//Reposotiry för Payment-entity

import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.entity.PaymentStatus;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
    //Hämtar en betalning endast om den tillhör den angivna användaren
    Optional<Payment> findByIdAndUserId(Long id, Long userId);

    //Hämtar användarens betalningar med den senaste först
    List<Payment> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    //Hämtar alla betalningsförsök för en order
    List<Payment> findAllByOrderIdOrderByCreatedAtDesc(Long orderId);

    //Hämtar en betakning genom dess unika transationsreferens
    Optional<Payment> findByTransactionReference(String transactionReference);

    //Kontrollerar om en order redan har en betalning
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);

}
