package com.ecommerceapp.userservice.models;

public class OrderDTO {

    private Integer orderId;

    private String userEmail;

    public OrderDTO(Integer userId, String userEmail) {
        this.orderId = userId;
        this.userEmail = userEmail;
    }

    public OrderDTO() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
