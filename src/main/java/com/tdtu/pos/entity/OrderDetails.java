package com.tdtu.pos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetails {

    @EmbeddedId
    private OrderDetailsId id; // Composite key

    @ManyToOne
    @MapsId("orderId") // Maps to the orderId field in OrderDetailsId
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId") // Maps to the productId field in OrderDetailsId
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private double unitPrice;

    // Getters and Setters
    public OrderDetailsId getId() {
        return id;
    }

    public void setId(OrderDetailsId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
