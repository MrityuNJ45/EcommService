package com.ecommerceapp.userservice.controllers;

import com.ecommerceapp.userservice.models.Product;
import com.ecommerceapp.userservice.service.ProductClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get/all")
    public List<Product> getAllProduct() {
        return productClientService.getAllProducts();
    }

    @PutMapping("/update/quantity/{id}/{quantity}")
    public Product updateQuantity(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity){
        return productClientService.updateQuantity(id,quantity);
    }

    @PutMapping("/decrease/quantity/{id}")
    public void decreaseQuantity(@PathVariable("id") Integer id) {
        productClientService.decreaseQuantity(id);
    }


}
