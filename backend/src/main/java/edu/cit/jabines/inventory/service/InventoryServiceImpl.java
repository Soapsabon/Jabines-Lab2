package edu.cit.jabines.inventory.service;

import edu.cit.jabines.inventory.model.Inventory;
import edu.cit.jabines.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory getItem(String productId) {
        return inventoryRepository.findById(productId).orElse(null);
    }

    @Override
    @Transactional
    public boolean reserve(String productId, int quantity) {
        Inventory item = getItem(productId);

        if (item == null) {
            return false;
        }

        if (item.getStock() < quantity) {
            return false;
        }

        item.setStock(item.getStock() - quantity);
        inventoryRepository.save(item);
        return true;
    }
}
