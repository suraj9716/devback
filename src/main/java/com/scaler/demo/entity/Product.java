package com.scaler.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;

    private String title;
    private String description;
    private String price;
    private String discount;
    private String discountedPrice;

    // Constructors
    public Product() {}

    public Product(String productId, String title, String description, String price, String discount, String discountedPrice) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
    }

    // Getters & Setters

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getDiscount() { return discount; }
    public void setDiscount(String discount) { this.discount = discount; }

    public String getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(String discountedPrice) { this.discountedPrice = discountedPrice; }
}
