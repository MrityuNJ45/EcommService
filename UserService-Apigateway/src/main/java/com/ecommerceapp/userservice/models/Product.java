package com.ecommerceapp.userservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {

    private Integer id;
    private String name;
    private Double price;
    private Integer quantity;

}
