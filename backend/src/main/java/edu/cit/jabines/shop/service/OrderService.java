package edu.cit.jabines.shop.service;

import edu.cit.jabines.inventory.service.InventoryService;
import edu.cit.jabines.shop.event.OrderPlacedEvent;
import edu.cit.jabines.shop.event.OrderRejectedEvent;
import edu.cit.jabines.shop.model.Order;
import edu.cit.jabines.shop.model.OrderItem;
import edu.cit.jabines.shop.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository,
                         InventoryService inventoryService,
                         ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order placeOrder(List<OrderItem> requestedItems) {
        // Step 1: validate ALL items before reserving ANY of them
        for (OrderItem requested : requestedItems) {
            if (!inventoryService.hasStock(requested.getProductId(), requested.getQuantity())) {
                Order rejected = new Order();
                rejected.setStatus("REJECTED");
                rejected.setReason("Insufficient stock for " + requested.getProductId());
                for (OrderItem item : requestedItems) {
                    rejected.addItem(new OrderItem(item.getProductId(), item.getQuantity()));
                }
                orderRepository.save(rejected);
                eventPublisher.publishEvent(new OrderRejectedEvent(rejected));
                return rejected;
            }
        }

        // Step 2: all validated - now actually reserve each item
        Order confirmed = new Order();
        confirmed.setStatus("CONFIRMED");
        confirmed.setReason("Order confirmed");
        for (OrderItem item : requestedItems) {
            confirmed.addItem(new OrderItem(item.getProductId(), item.getQuantity()));
            inventoryService.reserve(item.getProductId(), item.getQuantity());
        }
        orderRepository.save(confirmed);
        eventPublisher.publishEvent(new OrderPlacedEvent(confirmed));
        return confirmed;
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        if ("CANCELLED".equals(order.getStatus())) {
            throw new IllegalStateException("Order already cancelled: " + orderId);
        }

        // Only confirmed orders actually reserved stock, so only those get restocked
        if ("CONFIRMED".equals(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                inventoryService.restock(item.getProductId(), item.getQuantity());
            }
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}