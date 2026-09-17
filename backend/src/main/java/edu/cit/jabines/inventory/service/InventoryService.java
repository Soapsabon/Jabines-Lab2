package edu.cit.jabines.inventory.service;

import edu.cit.jabines.inventory.model.Inventory;
import java.util.List;

public interface InventoryService {

    List<Inventory> getAllInventory();

    boolean hasStock(String productId, int quantity);

    void reserve(String productId, int quantity);

    void restock(String productId, int quantity);
}