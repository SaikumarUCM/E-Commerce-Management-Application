package com.saikumar.microservice.inventoryservice.service;

import com.saikumar.microservice.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode,Integer quantity) {
        // find the inventory by skuCode where quantity>=1
        // return true if found, else false
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

}
