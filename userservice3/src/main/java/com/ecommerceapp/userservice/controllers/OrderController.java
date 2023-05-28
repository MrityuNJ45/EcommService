package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.models.Order;
import com.ecommerceapp.userservice.models.OrderDTO;
import com.ecommerceapp.userservice.service.OrderClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderClientService orderClientService;

    @PostMapping("/create")
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        return orderClientService.createOrder(orderDTO);
    }

    @GetMapping("/get/{userEmail}")
    public Order getOrder(@PathVariable("userEmail") String email) {
        return orderClientService.getOrder(email);
    }

}
