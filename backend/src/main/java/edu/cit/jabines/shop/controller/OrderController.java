package edu.cit.jabines.shop.controller;

import edu.cit.jabines.inventory.service.InventoryService;
import edu.cit.jabines.shop.model.Order;
import edu.cit.jabines.shop.model.OrderItem;
import edu.cit.jabines.shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final InventoryService inventoryService;

    public OrderController(OrderService orderService, InventoryService inventoryService) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
    }

    // POST /api/orders  { "items": [ { "productId": "P100", "quantity": 2 }, ... ] }
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        List<OrderItem> requestedItems = request.items().stream()
                .map(i -> new OrderItem(i.productId(), i.quantity()))
                .toList();

        Order result = orderService.placeOrder(requestedItems);

        return ResponseEntity.ok(Map.of(
                "orderId", result.getOrderId(),
                "status", result.getStatus(),
                "reason", result.getReason(),
                "items", result.getItems(),
                "inventory", inventoryService.getAllInventory()
        ));
    }

    // POST /api/orders/{orderId}/cancel
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Order cancelled = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(Map.of(
                    "orderId", cancelled.getOrderId(),
                    "status", cancelled.getStatus(),
                    "inventory", inventoryService.getAllInventory()
            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Request DTOs
    public record OrderRequest(List<OrderItemRequest> items) {
    }

    public record OrderItemRequest(String productId, int quantity) {
    }
}