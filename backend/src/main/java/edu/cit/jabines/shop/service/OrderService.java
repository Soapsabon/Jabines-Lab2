package edu.cit.jabines.shop.service;

import edu.cit.jabines.inventory.model.Inventory;
import edu.cit.jabines.inventory.service.InventoryService;
import edu.cit.jabines.shop.model.Order;
import edu.cit.jabines.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    @Autowired
    public OrderService(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public Order placeOrder(String productId, int quantity) {
        // Check if item exists
        Inventory item = inventoryService.getItem(productId);
        if (item == null) {
            Order order = new Order(productId, quantity, "REJECTED", "Product not found");
            return orderRepository.save(order);
        }

        // Try to reserve
        boolean reserved = inventoryService.reserve(productId, quantity);

        Order order;
        if (reserved) {
            order = new Order(productId, quantity, "CONFIRMED", "Order confirmed");
        } else {
            order = new Order(productId, quantity, "REJECTED", "Insufficient stock");
        }

        return orderRepository.save(order);
    }
}
