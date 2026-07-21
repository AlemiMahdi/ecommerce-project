package com.ecommerce.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity som representerar lagerinformation för en produkt.
 *
 * Inventory-service äger lagerdata.
 * Den sparar bara productId som referens till product-service.
 */
@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    
    //Primärnyckel för inventory-raden
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ID för produkten från product-service, detta är inte en foreign key till product_db
    @Column(nullable = false, unique = true)
    private Long productId;

    //Antal produkter som finns tillgängliga att köpa
    @Column(nullable = false)
    private Integer quantityAvailable;

    //Antal produkter som är reserverade i orders
    @Column(nullable = false)
    private Integer quantityReserved;

    //När inventory-raden skapades
    private LocalDateTime createdAt;

    //När inventory-raden senast uppdaterades
    private LocalDateTime updatedAt;

    //Körs automatisk innan raden sparas första gången
    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.quantityReserved == null) {
            this.quantityReserved = 0;
        }
    }

    //Körs automatiskt innan raden uppdateras
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
