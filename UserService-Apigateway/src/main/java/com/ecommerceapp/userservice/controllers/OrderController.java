package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.models.Order;
import com.ecommerceapp.userservice.models.OrderDTO;
import com.ecommerceapp.userservice.service.OrderClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderClientService orderClientService;

    @PostMapping("/create/{productId}")
    public Order createOrder(@PathVariable("productId") Integer productId, @AuthenticationPrincipal(expression = "username") String email) {
        OrderDTO orderDTO = new OrderDTO(productId, email);
        return orderClientService.createOrder(orderDTO);
    }

    @GetMapping("/get/{orderId}")
    public Order getOrder(@PathVariable("orderId") Integer orderId) {
        return orderClientService.getOrder(orderId);
    }

    @GetMapping("/get/user/all")
    public List<Order> getAllOrdersOfUserHandler(@AuthenticationPrincipal(expression = "username") String userEmail) {
        return orderClientService.getAllOrdersOfUser(userEmail);
    }

    @GetMapping("/get/all")
    public List<Order> getAllOrdersHandler() {
        return orderClientService.getAllOrders();
    }

}
