package com.ecommerceapp.orderservice.model;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    private String userEmail;

    private LocalDateTime createdAt;

    public Order(Integer productId, String userEmail, LocalDateTime createdAt) {
        this.productId = productId;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }
}
