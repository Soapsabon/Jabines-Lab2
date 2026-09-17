package edu.cit.jabines.shop.event;

import edu.cit.jabines.shop.model.Order;

public class OrderPlacedEvent {

    private final Order order;

    public OrderPlacedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}