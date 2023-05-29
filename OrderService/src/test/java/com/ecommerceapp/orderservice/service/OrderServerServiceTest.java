package com.ecommerceapp.orderservice.service;

import com.ecommerceapp.Order;
import com.ecommerceapp.orderservice.repository.OrderRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServerServiceTest {
    @InjectMocks
    private OrderServerService orderServerService;
    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void expectsToCreateAnOrder() {

        int productId = 123;
        String userEmail = "test@example.com";
        Order.CreateOrderRequest request = Order.CreateOrderRequest.newBuilder()
                .setProductId(productId)
                .setUserEmail(userEmail)
                .build();

        com.ecommerceapp.orderservice.model.Order savedOrder = new com.ecommerceapp.orderservice.model.Order();
        savedOrder.setId(1);
        savedOrder.setProductId(productId);
        savedOrder.setUserEmail(userEmail);
        savedOrder.setCreatedAt(LocalDateTime.now());
        when(orderRepository.save(any(com.ecommerceapp.orderservice.model.Order.class))).thenReturn(savedOrder);

        StreamObserver<Order.OrderResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<Order.OrderResponse> responseCaptor = ArgumentCaptor.forClass(Order.OrderResponse.class);

        orderServerService.createOrder(request, responseObserver);

        verify(orderRepository).save(any(com.ecommerceapp.orderservice.model.Order.class));

        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();


        Order.OrderResponse response = responseCaptor.getValue();
        assertEquals(1, response.getId());
        assertEquals(productId, response.getProductId());
        assertEquals(userEmail, response.getUserEmail());

        LocalDateTime expectedCreatedAt = savedOrder.getCreatedAt();
        LocalDateTime actualCreatedAt = LocalDateTime.ofEpochSecond(
                response.getCreatedAt().getSeconds(),
                response.getCreatedAt().getNanos(),
                ZoneOffset.UTC
        );
        assertEquals(expectedCreatedAt, actualCreatedAt);
    }

    @Test
    public void expectsToGetAnOrderIfItExists() {

        int orderId = 1;
        com.ecommerceapp.orderservice.model.Order existingOrder = new com.ecommerceapp.orderservice.model.Order();
        existingOrder.setId(orderId);
        existingOrder.setProductId(123);
        existingOrder.setUserEmail("test@example.com");
        existingOrder.setCreatedAt(LocalDateTime.now());
        Optional<com.ecommerceapp.orderservice.model.Order> orderOptional = Optional.of(existingOrder);
        Order.GetOrderRequest request = Order.GetOrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();


        when(orderRepository.findById(orderId)).thenReturn(orderOptional);

        StreamObserver<Order.OrderResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<Order.OrderResponse> responseCaptor = ArgumentCaptor.forClass(Order.OrderResponse.class);

        orderServerService.getOrder(request, responseObserver);


        verify(orderRepository).findById(orderId);


        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        Order.OrderResponse response = responseCaptor.getValue();
        assertEquals(orderId, response.getId());
        assertEquals(existingOrder.getProductId(), response.getProductId());
        assertEquals(existingOrder.getUserEmail(), response.getUserEmail());

        LocalDateTime expectedCreatedAt = existingOrder.getCreatedAt();
        LocalDateTime actualCreatedAt = LocalDateTime.ofEpochSecond(
                response.getCreatedAt().getSeconds(),
                response.getCreatedAt().getNanos(),
                ZoneOffset.UTC
        );
        assertEquals(expectedCreatedAt, actualCreatedAt);
    }

    @Test
    public void expectsToGetInvalidArgrumentWhenInvalidOrderIdIsGiven() {

        int orderId = 1;
        Optional<com.ecommerceapp.orderservice.model.Order> orderOptional = Optional.empty();
        Order.GetOrderRequest request = Order.GetOrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(orderOptional);

        StreamObserver<Order.OrderResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<Throwable> errorCaptor = ArgumentCaptor.forClass(Throwable.class);

        orderServerService.getOrder(request, responseObserver);

        verify(orderRepository).findById(orderId);

        verify(responseObserver).onError(errorCaptor.capture());

        Throwable error = errorCaptor.getValue();
        assertTrue(error instanceof StatusRuntimeException);
        assertEquals(Status.INVALID_ARGUMENT.getCode(), ((StatusRuntimeException) error).getStatus().getCode());
        assertEquals("Order not found", ((StatusRuntimeException) error).getStatus().getDescription());
    }

    @Test
    public void expectsToGetAListOfOrdersOfUser() {

        String userEmail = "test@example.com";
        List<com.ecommerceapp.orderservice.model.Order> existingOrders = new ArrayList<>();

        existingOrders.add(new com.ecommerceapp.orderservice.model.Order(1, 123, userEmail,LocalDateTime.now()));
        existingOrders.add(new com.ecommerceapp.orderservice.model.Order(2, 456, userEmail, LocalDateTime.now()));
        existingOrders.add(new com.ecommerceapp.orderservice.model.Order(3, 789, userEmail, LocalDateTime.now()));
        when(orderRepository.findByUserEmail(userEmail)).thenReturn(existingOrders);


        Order.GetAllOrdersOfUserRequest request = Order.GetAllOrdersOfUserRequest.newBuilder()
                .setUserEmail(userEmail)
                .build();

        StreamObserver<Order.GetAllOrdersOfUserResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<Order.GetAllOrdersOfUserResponse> responseCaptor = ArgumentCaptor.forClass(Order.GetAllOrdersOfUserResponse.class);

        orderServerService.getAllOrdersOfUser(request, responseObserver);

        verify(orderRepository).findByUserEmail(userEmail);

        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        Order.GetAllOrdersOfUserResponse response = responseCaptor.getValue();
        assertEquals(existingOrders.size(), response.getOrdersCount());
        for (int i = 0; i < existingOrders.size(); i++) {
            Order.OrderResponse orderResponse = response.getOrders(i);
            com.ecommerceapp.orderservice.model.Order existingOrder = existingOrders.get(i);
            assertEquals(existingOrder.getId(), orderResponse.getId());
            assertEquals(existingOrder.getProductId(), orderResponse.getProductId());
            assertEquals(existingOrder.getUserEmail(), orderResponse.getUserEmail());
            LocalDateTime expectedCreatedAt = existingOrder.getCreatedAt();
            LocalDateTime actualCreatedAt = LocalDateTime.ofEpochSecond(
                    orderResponse.getCreatedAt().getSeconds(),
                    orderResponse.getCreatedAt().getNanos(),
                    ZoneOffset.UTC
            );
            assertEquals(expectedCreatedAt, actualCreatedAt);
        }
    }

    @Test
    public void expectsToGetNotFoundExceptionWhenNoOrdersAreThereForUser() {

        String userEmail = "test@example.com";
        List<com.ecommerceapp.orderservice.model.Order> emptyOrderList = new ArrayList<>();
        when(orderRepository.findByUserEmail(userEmail)).thenReturn(emptyOrderList);

        Order.GetAllOrdersOfUserRequest request = Order.GetAllOrdersOfUserRequest.newBuilder()
                .setUserEmail(userEmail)
                .build();

        StreamObserver<Order.GetAllOrdersOfUserResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<Throwable> errorCaptor = ArgumentCaptor.forClass(Throwable.class);

        orderServerService.getAllOrdersOfUser(request, responseObserver);

        verify(orderRepository).findByUserEmail(userEmail);

        verify(responseObserver).onError(errorCaptor.capture());

        Throwable error = errorCaptor.getValue();
        assertTrue(error instanceof StatusRuntimeException);
        assertEquals(Status.NOT_FOUND.getCode(), ((StatusRuntimeException) error).getStatus().getCode());
        assertEquals("No orders found", ((StatusRuntimeException) error).getStatus().getDescription());
    }



}