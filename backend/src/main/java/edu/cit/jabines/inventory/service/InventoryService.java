package edu.cit.jabines.inventory.service;

import edu.cit.jabines.inventory.model.Inventory;

public interface InventoryService {
    Inventory getItem(String productId);
    boolean reserve(String productId, int quantity);
}
