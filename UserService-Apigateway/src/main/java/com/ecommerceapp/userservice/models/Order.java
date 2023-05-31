package com.ecommerceapp.userservice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {

    private Integer id;
    private Integer productId;
    private String userEmail;
    private LocalDateTime createdAt;

}
