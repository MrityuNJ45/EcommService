package com.ecommerceapp.userservice.service;

import com.ecommerceapp.userservice.models.Customer;
import com.ecommerceapp.userservice.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer() {

        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer createdCustomer = customerService.createCustomer(customer);
        verify(customerRepository, times(1)).save(customer);
        assertEquals(customer, createdCustomer);
    }

    @Test
    public void testGetCustomerByEmail() {

        String email = "test@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        Customer retrievedCustomer = customerService.getCustomerByEmail(email);
        verify(customerRepository, times(1)).findByEmail(email);
        assertEquals(customer, retrievedCustomer);
    }
}