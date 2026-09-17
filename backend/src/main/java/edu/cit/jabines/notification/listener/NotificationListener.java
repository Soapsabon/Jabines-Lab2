package edu.cit.jabines.notification.listener;

import edu.cit.jabines.inventory.event.LowStockEvent;
import edu.cit.jabines.notification.model.Notification;
import edu.cit.jabines.notification.repository.NotificationRepository;
import edu.cit.jabines.shop.event.OrderPlacedEvent;
import edu.cit.jabines.shop.event.OrderRejectedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final NotificationRepository notificationRepository;

    public NotificationListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        String message = "Order " + event.getOrder().getOrderId() + " confirmed";
        notificationRepository.save(new Notification(message));
    }

    @EventListener
    public void onOrderRejected(OrderRejectedEvent event) {
        String message = "Order " + event.getOrder().getOrderId() + " rejected: " + event.getOrder().getReason();
        notificationRepository.save(new Notification(message));
    }

    @EventListener
    public void onLowStock(LowStockEvent event) {
        String message = "Reorder needed: " + event.getProductName() + " (" + event.getProductId()
                + ") - only " + event.getRemainingStock() + " left";
        notificationRepository.save(new Notification(message));
    }
}