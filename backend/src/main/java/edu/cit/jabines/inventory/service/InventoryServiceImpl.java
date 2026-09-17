package edu.cit.jabines.inventory.service;

import edu.cit.jabines.inventory.event.LowStockEvent;
import edu.cit.jabines.inventory.model.Inventory;
import edu.cit.jabines.inventory.repository.InventoryRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
class InventoryServiceImpl implements InventoryService {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    InventoryServiceImpl(InventoryRepository inventoryRepository, ApplicationEventPublisher eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public boolean hasStock(String productId, int quantity) {
        Inventory item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));
        return item.getStock() >= quantity;
    }

    @Override
    public void reserve(String productId, int quantity) {
        Inventory item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));

        item.setStock(item.getStock() - quantity);
        inventoryRepository.save(item);

        if (item.getStock() < LOW_STOCK_THRESHOLD) {
            eventPublisher.publishEvent(new LowStockEvent(item.getProductId(), item.getName(), item.getStock()));
        }
    }

    @Override
    public void restock(String productId, int quantity) {
        Inventory item = inventoryRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));

        item.setStock(item.getStock() + quantity);
        inventoryRepository.save(item);
    }
}