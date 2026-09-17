package edu.cit.jabines.shop.controller;

import edu.cit.jabines.inventory.model.Inventory;
import edu.cit.jabines.inventory.service.InventoryService;
import edu.cit.jabines.shop.model.Order;
import edu.cit.jabines.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final InventoryService inventoryService;
    private final JpaRepository<Inventory, String> inventoryRepository;

    @Autowired
    public OrderController(OrderService orderService, InventoryService inventoryService, JpaRepository<Inventory, String> inventoryRepository) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/inventory")
    public ResponseEntity<?> getInventory() {
        List<Inventory> inventory = inventoryRepository.findAll();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> payload) {
        String productId = (String) payload.get("productId");
        int quantity = ((Number) payload.get("quantity")).intValue();

        Order order = orderService.placeOrder(productId, quantity);

        // Get current inventory
        List<Inventory> inventory = inventoryRepository.findAll();

        return ResponseEntity.ok(Map.of(
                "status", order.getStatus(),
                "reason", order.getReason(),
                "inventory", inventory
        ));
    }
}