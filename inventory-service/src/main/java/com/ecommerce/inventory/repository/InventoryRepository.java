package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Repository för Inventory entity
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
    
    //Hämtar largerinformation baserat på productId
    Optional<Inventory> findByProductId(Long productId);

    //Kontrollerar om lager redan finns för en produkt
    boolean existsByProductId(Long productId);
}
