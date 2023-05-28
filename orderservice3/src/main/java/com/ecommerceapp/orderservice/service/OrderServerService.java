package com.ecommerceapp.orderservice.service;

import com.ecommerceapp.Order;
import com.ecommerceapp.OrderServiceGrpc;
import com.ecommerceapp.orderservice.repository.OrderRepository;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@GrpcService
public class OrderServerService extends OrderServiceGrpc.OrderServiceImplBase {

    @Autowired
    private OrderRepository orderRepository;
    @Override
    public void createOrder(Order.CreateOrderRequest request, StreamObserver<Order.OrderResponse> responseObserver) {
        com.ecommerceapp.orderservice.model.Order order = new com.ecommerceapp.orderservice.model.Order();
        order.setProductId(request.getProductId());
        order.setUserEmail(request.getUserEmail());
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        com.ecommerceapp.orderservice.model.Order savedOrder = new com.ecommerceapp.orderservice.model.Order(order.getId(), order.getProductId(), order.getUserEmail(), order.getCreatedAt());

        Timestamp orderTimeStamp = Timestamp.newBuilder()
                .setSeconds(savedOrder.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .setNanos(savedOrder.getCreatedAt().getNano()).build();

        Order.OrderResponse response = Order.OrderResponse.newBuilder()
                .setId(savedOrder.getId())
                .setProductId(savedOrder.getProductId())
                .setUserEmail(savedOrder.getUserEmail())
                .setCreatedAt(orderTimeStamp).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void getOrder(Order.GetOrderResponse request, StreamObserver<Order.OrderResponse> responseObserver) {
        String email = request.getUserEmail();
        com.ecommerceapp.orderservice.model.Order order = orderRepository.findByUserEmail(email);
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(order.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .setNanos(order.getCreatedAt().getNano()).build();

        Order.OrderResponse response = Order.OrderResponse.newBuilder()
                .setId(order.getId())
                .setProductId(order.getProductId())
                .setUserEmail(order.getUserEmail())
                        .setCreatedAt(timestamp).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
