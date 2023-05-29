package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.config.SecurityConfig;
import com.ecommerceapp.userservice.controllers.CustomerController;
import com.ecommerceapp.userservice.models.Customer;
import com.ecommerceapp.userservice.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private CustomerService customerService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Authentication authentication;

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void expectsToGetOkWhenSavingAValidCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");
        customer.setRole("USER");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customer/create")
                        .contentType("application/json")
                        .content("{\"email\": \"test@example.com\", \"role\": \"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    public void expectsToGetBadRequestWhenInvalidCustomerIsAdded() throws Exception {
        mockMvc.perform(post("/customer/create")
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(customerService);
    }

    @Test
    public void expectsToGetBadRequestWhenInvalidRoleIsAdded() throws Exception {
        mockMvc.perform(post("/customer/create")
                        .contentType("application/json")
                        .content("{\"email\": \"test@example.com\", \"password\": \"password\", \"role\": \"INVALID_ROLE\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(customerService);
    }

    @Test
    public void expectsToGetOkStatusWhenGettingACustomer() throws Exception {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");

        when(customerService.getCustomerByEmail("test@example.com")).thenReturn(customer);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        mockMvc.perform(get("/customer/get").with(user(customer.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    public void expectsToGetUnauthorisedStatusWhenAuthenticationIsNotThere() throws Exception {
        mockMvc.perform(get("/customer/get"))
                .andExpect(status().isUnauthorized());
    }
}
