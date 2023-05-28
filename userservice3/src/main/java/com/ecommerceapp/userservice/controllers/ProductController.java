package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.models.Product;
import com.ecommerceapp.userservice.service.ProductClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductClientService productClientService;

    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return productClientService.createProduct(product);
    }

    @GetMapping("/get/{id}")
    public Product getProduct(@PathVariable("id") Integer id) {
        return productClientService.getProduct(id);
    }


}
