package com.ecommerceapp.orderservice.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {


    @Test
    public void expectsToCreateAnOrder() {
        assertDoesNotThrow(() -> {
            new Order(1,"a@gmail.com", LocalDateTime.now());
        });
    }

}