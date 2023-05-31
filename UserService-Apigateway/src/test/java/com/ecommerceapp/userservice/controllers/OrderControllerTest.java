package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.config.SecurityConfig;
import com.ecommerceapp.userservice.exceptions.ProductException;
import com.ecommerceapp.userservice.models.Order;
import com.ecommerceapp.userservice.models.OrderDTO;
import com.ecommerceapp.userservice.service.OrderClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(SecurityConfig.class)
@SpringBootTest
class OrderControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderClientService orderClientService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    public void expectsToCreateAnOrderWithValidUser() throws Exception {
        Order order = new Order();
        when(orderClientService.createOrder(any(OrderDTO.class))).thenReturn(order);
        mockMvc.perform(post("/order/create/{productId}", 1)
                        .with(user("test@example.com"))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrder() throws Exception {
        Order order = new Order();

        when(orderClientService.getOrder(1)).thenReturn(order);

        mockMvc.perform(get("/order/get/{orderId}", 1).with(user("validUser@gmail.com")))
                .andExpect(status().isOk());

    }

    @Test
    public void expectsToGetUnauthorisedStatusWhenAuthenticationIsNotThereWhileGettingOrder() throws Exception {
        Order order = new Order();

        when(orderClientService.getOrder(1)).thenReturn(order);

        mockMvc.perform(get("/order/get/{orderId}", 1))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void expectsToGetOkStatusWhenAuthenticationIsThereWhileGettingAllOrdersOfAParticularUser() throws Exception {
        List<Order> orders = new ArrayList<>();

        when(orderClientService.getAllOrdersOfUser("test@example.com")).thenReturn(orders);

        mockMvc.perform(get("/order/get/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());;

    }

    @Test
    public void expectsToGetUnauthorisedStatusWhenAuthenticationIsNotThereWhileGettingAllOrdersOfAParticularUser() throws Exception {
        List<Order> orders = new ArrayList<>();

        when(orderClientService.getAllOrdersOfUser("test@example.com")).thenReturn(orders);

        mockMvc.perform(get("/order/get/user/all"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void expectsToGetOkStatusWhenAdminTryingToGetAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        when(orderClientService.getAllOrders()).thenReturn(orders);
        mockMvc.perform(get("/order/get/all").with(user("admin@gmail.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void expectsToGetForbiddenStatusWhenNormalUserTryingToGetAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        when(orderClientService.getAllOrders()).thenReturn(orders);
        mockMvc.perform(get("/order/get/all").with(user("customer@gmail.com").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void expectsToGetBadRequestWhenAProductExceptionIsThrownFromTheOrderClientServiceWhenPlacingAnOrder() throws Exception {
        when(orderClientService.createOrder(any(OrderDTO.class))).thenThrow(new ProductException());
        mockMvc.perform(post("/order/create/{productId}", 1).with(user("customer@gmail.com"))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }


}