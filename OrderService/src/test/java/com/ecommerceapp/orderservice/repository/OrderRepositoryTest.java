package com.ecommerceapp.orderservice.repository;

import com.ecommerceapp.orderservice.model.Order;
import com.ecommerceapp.orderservice.service.OrderServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @InjectMocks
    private OrderServerService orderServerService;
    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void expectsToGetAListOfOrdersByGivingEmail() {

        Order order1 = new Order(1, 123, "user1@example.com", LocalDateTime.now());
        Order order2 = new Order(2, 456, "user2@example.com", LocalDateTime.now());
        Order order3 = new Order(3, 789, "user1@example.com", LocalDateTime.now());
        orderRepository.saveAll(List.of(order1, order2, order3));
        Mockito.when(orderRepository.findByUserEmail("user1@example.com")).thenReturn(List.of(order1, order3));
        List<Order> foundOrders = orderRepository.findByUserEmail("user1@example.com");

        assertEquals(2, foundOrders.size());
        assertTrue(foundOrders.contains(order1));
        assertTrue(foundOrders.contains(order3));
    }

    @Test
    public void expectsToGetEmptyListWhenNoOrderIsPresentThereWithUserEmail() {
        List<Order> foundOrders = orderRepository.findByUserEmail("user1@example.com");
        assertTrue(foundOrders.isEmpty());
    }

}