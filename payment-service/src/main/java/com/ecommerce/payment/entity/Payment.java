package com.ecommerce.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.*;

//Entity som representera en betalning
@Entity
@Table(name = "payments",
    indexes = {
        @Index(name = "idx_payment_order_id", columnList = "orderId"),
        @Index(name = "idx_payment_user_id", columnList = "userId")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    
    //Primärnyckeln
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID för ordern i order-service.
     *
     * Detta är inte en foreign key eftersom ordern ligger
     * i en annan mikrotjänst och databas.
     */
    @Column(nullable = false)
    private Long orderId;

    //ID för användaren som äger betalningen, värder komemr senare från X-User-Id headern
    @Column(nullable = false)
    private Long userId;

    //Orderns totala belopp, värdet ska hämtar från order-service och ska inte acceptera direkt från klienten
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    //Kundens valda betalningsmetod
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    //Betalningens nuvarande status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    //Unik referens för betalningen
    @Column(nullable = false, unique = true, updatable = false, length = 50)
    private String transactionReference;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = PaymentStatus.PENDING;
        }

        if (this.transactionReference == null || this.transactionReference.isBlank()) {
            this.transactionReference = "PAY" + UUID.randomUUID();
        }
    }

    //Körs automatiskt nör betalningen uppdateras
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
