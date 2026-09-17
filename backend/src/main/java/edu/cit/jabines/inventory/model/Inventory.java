package edu.cit.jabines.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private int stock;

    public Inventory() {
    }

    public Inventory(String productId, String name, int stock) {
        this.productId = productId;
        this.name = name;
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
