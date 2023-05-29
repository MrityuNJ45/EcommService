package com.ecommerceapp.orderservice.service;

import com.ecommerceapp.Order;
import com.ecommerceapp.OrderServiceGrpc;
import com.ecommerceapp.orderservice.exceptions.OrderException;
import com.ecommerceapp.orderservice.repository.OrderRepository;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        com.ecommerceapp.orderservice.model.Order savedOrder = orderRepository.save(order);

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
    public void getOrder(Order.GetOrderRequest request, StreamObserver<Order.OrderResponse> responseObserver) {

        try {

            Integer orderId = request.getOrderId();
            Optional<com.ecommerceapp.orderservice.model.Order> opt = orderRepository.findById(orderId);


            if (!opt.isPresent()) {

                throw new OrderException("Order not found");
            }

            com.ecommerceapp.orderservice.model.Order order = opt.get();

            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(order.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                    .setNanos(order.getCreatedAt().getNano())
                    .build();


            Order.OrderResponse response = Order.OrderResponse.newBuilder()
                    .setId(order.getId())
                    .setProductId(order.getProductId())
                    .setUserEmail(order.getUserEmail())
                    .setCreatedAt(timestamp)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch (OrderException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllOrdersOfUser(Order.GetAllOrdersOfUserRequest request, StreamObserver<Order.GetAllOrdersOfUserResponse> responseObserver) {

        try {
            String userEmail = request.getUserEmail();
            List<com.ecommerceapp.orderservice.model.Order> orders = orderRepository.findByUserEmail(userEmail);

            if (orders.size() == 0) {
                throw new OrderException("No orders found");
            }
            List<Order.OrderResponse> orderResponseList = new ArrayList<>();
            for (com.ecommerceapp.orderservice.model.Order order : orders) {
                Timestamp timestamp = Timestamp.newBuilder()
                        .setSeconds(order.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                        .setNanos(order.getCreatedAt().getNano())
                        .build();


                Order.OrderResponse orderResponse = Order.OrderResponse.newBuilder()
                        .setId(order.getId())
                        .setProductId(order.getProductId())
                        .setUserEmail(order.getUserEmail())
                        .setCreatedAt(timestamp)
                        .build();
                orderResponseList.add(orderResponse);
            }
            Order.GetAllOrdersOfUserResponse response = Order.GetAllOrdersOfUserResponse.newBuilder().addAllOrders(orderResponseList).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (OrderException orderException) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(orderException.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.UNKNOWN.withDescription(e.getMessage()).asRuntimeException());
        }

    }

    @Override
    public void getAllOrders(Order.GetAllOrdersRequest request, StreamObserver<Order.GetAllOrdersResponse> responseObserver) {

        try {
            List<com.ecommerceapp.orderservice.model.Order> allOrders = orderRepository.findAll();

            if (allOrders.size() == 0) {
                throw new OrderException("No orders found");
            }

            List<Order.OrderResponse> orderResponseList = new ArrayList<>();
            for (com.ecommerceapp.orderservice.model.Order order : allOrders) {
                Timestamp timestamp = Timestamp.newBuilder()
                        .setSeconds(order.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                        .setNanos(order.getCreatedAt().getNano())
                        .build();


                Order.OrderResponse orderResponse = Order.OrderResponse.newBuilder()
                        .setId(order.getId())
                        .setProductId(order.getProductId())
                        .setUserEmail(order.getUserEmail())
                        .setCreatedAt(timestamp)
                        .build();
                orderResponseList.add(orderResponse);

            }
            Order.GetAllOrdersResponse response = Order.GetAllOrdersResponse.newBuilder().addAllOrders(orderResponseList).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (OrderException orderException) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(orderException.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.UNKNOWN.withDescription(e.getMessage()).asRuntimeException());
        }

    }
}
