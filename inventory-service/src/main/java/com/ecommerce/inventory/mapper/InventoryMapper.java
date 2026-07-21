package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.entity.Inventory;

//Mapper som konverterar mellan Inventory entity och DTOs
public class InventoryMapper {
    
    //Konverterar InventoryRequest till Inventory entity
    public static Inventory toEntity(InventoryRequest request){
        return Inventory.builder()
                .productId(request.getProductId())
                .quantityAvailable(request.getQuantityAvailable())
                .quantityReserved(0)
                .build();
    }

    //Konvertrar Inventory entity till InventoryResponse DTO
    public static InventoryResponse toResponse(Inventory inventory){
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantityAvailable(inventory.getQuantityAvailable())
                .quantityReserved(inventory.getQuantityReserved())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
