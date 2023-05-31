package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.exceptions.CustomerException;
import com.ecommerceapp.userservice.models.Customer;
import com.ecommerceapp.userservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if(customer == null) {
            throw new CustomerException("Invalid customer provided");
        }

        if(customer.getRole().toUpperCase().equals("ADMIN") || customer.getRole().toUpperCase().equals("USER")) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setRole(customer.getRole().toUpperCase());

            return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
        }

        throw new CustomerException("Invalid role provided");

    }

    @GetMapping("/get")
    public Customer getCustomer(@AuthenticationPrincipal(expression = "username") String email) {
        return customerService.getCustomerByEmail(email);
    }
}
