package com.ecommerce.inventory.service;

import org.springframework.stereotype.Service;

import com.ecommerce.inventory.dto.ConfirmInventoryRequest;
import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.ReleaseInventoryRequest;
import com.ecommerce.inventory.dto.ReserveInventoryRequest;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exception.InsufficientInventoryException;
import com.ecommerce.inventory.exception.InventoryAlreadyExistsException;
import com.ecommerce.inventory.exception.InventoryNotFoundException;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

//Affärslogiken för lagerhantering
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

    //Skapr en ny invetory-rad för en produkt
    @Override
    public InventoryResponse createInventory(InventoryRequest request) {
        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new InventoryAlreadyExistsException(request.getProductId());
        }

        Inventory inventory = InventoryMapper.toEntity(request);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toResponse(savedInventory);
    }

    //Hämtar lager baserat på productId
    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new InventoryNotFoundException(productId));
        return InventoryMapper.toResponse(inventory);
    }

    /**
     * Reserverar lager.
     *
     * Exempel:
     * quantityAvailable: 10
     * quantityReserved: 0
     *
     * Om vi reserverar 2:
     * quantityAvailable: 8
     * quantityReserved: 2
     */
    @Override
    public InventoryResponse reserveInventory(ReserveInventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new InventoryNotFoundException(request.getProductId()));
        
        if (inventory.getQuantityAvailable() < request.getQuantity()) {
            throw new InsufficientInventoryException(
                "Not enough inventory for the product id" + request.getProductId()
            );
        }
        
        inventory.setQuantityAvailable(
                inventory.getQuantityAvailable() - request.getQuantity()
        );

        inventory.setQuantityReserved(
            inventory.getQuantityReserved() + request.getQuantity()
        );

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toResponse(updatedInventory);
    }


    /**
     * Släpper tillbaka reserverat lager.
     *
     * Exempel:
     * quantityAvailable: 8
     * quantityReserved: 2
     *
     * Om vi släpper 2:
     * quantityAvailable: 10
     * quantityReserved: 0
     */
    @Override
    public InventoryResponse releaseInventory(ReleaseInventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new InventoryNotFoundException(request.getProductId()));
        
        if (inventory.getQuantityReserved() < request.getQuantity()) {
            throw new InsufficientInventoryException(
                "Cannot release more inventory than reserved for the product id" + request.getProductId());
        }

        inventory.setQuantityReserved(
            inventory.getQuantityReserved() - request.getQuantity()
        );

        inventory.setQuantityAvailable(
            inventory.getQuantityAvailable() + request.getQuantity()
        );

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toResponse(updatedInventory);
    }
    
    /**
     * Bekräftar reserverat lager som slutgiltigt sålt.
     *
     * quantityAvailable ändras inte eftersom den redan minskades
     * när lagret reserverades.
     */
    @Override
    public InventoryResponse confirmInventory(ConfirmInventoryRequest request){

        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> 
                    new InventoryNotFoundException(request.getProductId()));
        
        if (inventory.getQuantityReserved() < request.getQuantity()) {
            throw new InsufficientInventoryException(
                "Cannot confirm more inventory than reserved for the product id: "
                    + request.getProductId()
            );
        }
        inventory.setQuantityReserved(
            inventory.getQuantityReserved() - request.getQuantity()
        );

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toResponse(updatedInventory);
    }

}
