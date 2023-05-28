package com.ecommerceapp.userservice.models;

public class OrderDTO {

    private Integer productId;

    private String userEmail;

    public OrderDTO(Integer userId, String userEmail) {
        this.productId = userId;
        this.userEmail = userEmail;
    }

    public OrderDTO() {
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
}
