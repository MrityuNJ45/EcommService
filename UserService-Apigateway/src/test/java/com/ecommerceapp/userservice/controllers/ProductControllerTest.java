package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.config.SecurityConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@@Import(SecurityConfig.class)
class ProductControllerTest {

}