package com.ecommerceapp.userservice.models;

public class OrderDTO {

    private Integer userId;

    private String userEmail;

    public OrderDTO(Integer userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public OrderDTO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
