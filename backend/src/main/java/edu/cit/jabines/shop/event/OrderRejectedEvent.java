package edu.cit.jabines.shop.event;

import edu.cit.jabines.shop.model.Order;

public class OrderRejectedEvent {

    private final Order order;

    public OrderRejectedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}