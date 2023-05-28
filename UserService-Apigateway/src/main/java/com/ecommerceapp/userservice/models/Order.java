package com.ecommerceapp.userservice.models;


import java.time.LocalDateTime;


public class Order {


    private Integer id;

    private Integer productId;

    private String userEmail;

    private LocalDateTime createdAt;

    public Order(Integer id, Integer productId, String userEmail, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
