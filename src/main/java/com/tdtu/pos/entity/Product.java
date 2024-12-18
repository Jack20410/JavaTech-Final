package com.tdtu.pos.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private double retailPrice;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private int quantity ;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String barcode;

    private LocalDateTime createdDate = LocalDateTime.now();
    private String imagePath;

    public Product() {}

    public Product(int id, String name, String category, double retailPrice,
                   boolean isAvailable, int quantity, String description,
                   String barcode, LocalDateTime createdDate, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.retailPrice = retailPrice;
        this.isAvailable = isAvailable;
        this.quantity = quantity;
        this.description = description;
        this.barcode = barcode;
        this.createdDate = createdDate;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}