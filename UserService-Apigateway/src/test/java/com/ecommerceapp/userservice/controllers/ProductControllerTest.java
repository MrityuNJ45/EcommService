package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.config.SecurityConfig;
import com.ecommerceapp.userservice.models.Product;
import com.ecommerceapp.userservice.service.ProductClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ProductClientService productClientService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "admin@gmai.com", roles = "ADMIN")
    public void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");

        when(productClientService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"name\": \"Test Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

       verify(productClientService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void expectsToGiveForbiddenWhenUserTriesToAddProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");

        when(productClientService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"name\": \"Test Product\"}"))
                .andExpect(status().isForbidden());
    }

    @Test

    public void expectsToGiveUnauthorisedWhenWithoutAuthorisationTriesToAddProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");

        when(productClientService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"name\": \"Test Product\"}"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "valid@gmail.com")
    public void expectsToGetOkWhenValidUserTriesToGetProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");

        when(productClientService.getProduct(1)).thenReturn(product);

        mockMvc.perform(get("/product/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productClientService, times(1)).getProduct(1);
    }

    @Test
    @WithMockUser(username = "valid@gmail.com")
    public void expectsToGetOkWhenValidUserTriesToGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);

        when(productClientService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/product/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productClientService, times(1)).getAllProducts();
    }




}