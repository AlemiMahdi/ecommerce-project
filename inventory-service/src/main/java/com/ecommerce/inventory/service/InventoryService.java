package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.ReleaseInventoryRequest;
import com.ecommerce.inventory.dto.ReserveInventoryRequest;

//Interface för inventory-service
public interface InventoryService {
    
    //Skapar lager för en produkt
    InventoryResponse createInventory(InventoryRequest request);

    //Hämtar lagerinformation för en produkt
    InventoryResponse getInventoryByProductId(Long productId);

    //Reserverar lager när en order skapas
    InventoryResponse reserveInventory(ReserveInventoryRequest request);

    //Släppger tillbaka reserverat lager, exempelvis om en order avbryts
    InventoryResponse releaseInventory(ReleaseInventoryRequest request);
    

}
