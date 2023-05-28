package com.ecommerceapp.userservice.service;

import com.ecommerceapp.Order;
import com.ecommerceapp.OrderServiceGrpc;
import com.ecommerceapp.ProductServiceGrpc;
import com.ecommerceapp.userservice.exceptions.ProductException;
import com.ecommerceapp.userservice.models.OrderDTO;

import com.ecommerceapp.userservice.models.Product;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class OrderClientService {


    @GrpcClient("grpc-order-service")
    OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

//    @GrpcClient("grpc-product-service")
//    ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @Autowired
    private ProductClientService productClientService;


    public com.ecommerceapp.userservice.models.Order createOrder(OrderDTO orderDTO) {

        Product product = productClientService.getProduct(orderDTO.getUserId());
        if(product.getQuantity() < 1) {
            throw new ProductException("Product out of stock");
        }

        Order.CreateOrderRequest createOrderRequest = Order.CreateOrderRequest.newBuilder()
                .setProductId(orderDTO.getUserId())
                .setUserEmail(orderDTO.getUserEmail()).build();
        Order.OrderResponse response = orderServiceBlockingStub.createOrder(createOrderRequest);
        LocalDateTime timeOfOrder = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getCreatedAt().getSeconds(), response.getCreatedAt().getNanos()), ZoneOffset.UTC);
        com.ecommerceapp.userservice.models.Order modelResponse = new com.ecommerceapp.userservice.models.Order(response.getId(), response.getProductId(), response.getUserEmail(), timeOfOrder);
        return modelResponse;
    }

    public com.ecommerceapp.userservice.models.Order getOrder(String userEmail) {
        Order.GetOrderResponse getOrderRequest = Order.GetOrderResponse.newBuilder()
                .setUserEmail(userEmail).build();

        Order.OrderResponse response =orderServiceBlockingStub.getOrder(getOrderRequest);
        LocalDateTime timeOfOrder = LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getCreatedAt().getSeconds(), response.getCreatedAt().getNanos()), ZoneOffset.UTC);
        com.ecommerceapp.userservice.models.Order modelResponse = new com.ecommerceapp.userservice.models.Order(response.getId(), response.getProductId(), response.getUserEmail(), timeOfOrder);
        return modelResponse;
    }

}
