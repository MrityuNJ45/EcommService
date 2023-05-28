package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.models.Order;
import com.ecommerceapp.userservice.models.OrderDTO;
import com.ecommerceapp.userservice.service.OrderClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderClientService orderClientService;

    @PostMapping("/create")
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        return orderClientService.createOrder(orderDTO);
    }

    @GetMapping("/get/{orderId}")
    public Order getOrder(@PathVariable("orderId") Integer orderId) {
        return orderClientService.getOrder(orderId);
    }

    @GetMapping("/get/all/{userEmail}")
    public List<Order> getAllOrdersOfUserHandler(@PathVariable("userEmail") String userEmail) {
        return orderClientService.getAllOrdersOfUser(userEmail);
    }

    @GetMapping("/get/all")
    public List<Order> getAllOrdersHandler() {
        return orderClientService.getAllOrders();
    }

}
