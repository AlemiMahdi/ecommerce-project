package com.ecommerce.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.inventory.dto.ConfirmInventoryRequest;
import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.ReleaseInventoryRequest;
import com.ecommerce.inventory.dto.ReserveInventoryRequest;
import com.ecommerce.inventory.service.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//Controller för inventory-serivce, här finns API-endpoints för lagerhantering
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;

    //Skapar en lager för en produkt : POST /api/v1/inventory
    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
        @Valid @RequestBody InventoryRequest request
    ){
        InventoryResponse response = inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Hämtar lagerinformation för en product : GET /api/v1/inventory/{productId}
    @GetMapping("/{productId}")
    public ResponseEntity <InventoryResponse> getInventoryByProductId(
        @PathVariable Long productId
    ){
        InventoryResponse response = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(response);
    }

    //Reserverar lager : POST /api/v1/inventory/reserve
    @PostMapping("/reserve")
    public ResponseEntity<InventoryResponse> reserveInventory(
        @Valid @RequestBody ReserveInventoryRequest request
    ){
        InventoryResponse response = inventoryService.reserveInventory(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/release")
    public ResponseEntity<InventoryResponse> releaseInventory(
        @Valid @RequestBody ReleaseInventoryRequest request
    ){
        InventoryResponse response = inventoryService.releaseInventory(request);
        return ResponseEntity.ok(response);
    }

    //Bekräftar reserverat lager som sålt : POST /api/v1/inventory/confirm
    @PostMapping("/confirm")
    public ResponseEntity<InventoryResponse> confirmInventory(
        @Valid @RequestBody ConfirmInventoryRequest request
    ){
        InventoryResponse response = inventoryService.confirmInventory(request);
        return ResponseEntity.ok(response);
    }

}
