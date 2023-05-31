package com.ecommerceapp.orderservice.service;


import com.ecommerceapp.orderservice.model.Order;
import com.ecommerceapp.orderservice.repository.OrderRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServerServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderGrpcServerService orderGrpcServerService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    public void createOrder_ValidRequest_ReturnsResponse() {
        // Arrange
        Integer productId = 123;
        String userEmail = "test@example.com";
        LocalDateTime currentDateTime = LocalDateTime.now();

        com.ecommerceapp.orderservice.model.Order savedOrder = new com.ecommerceapp.orderservice.model.Order(productId, userEmail, currentDateTime);
        savedOrder.setId(1);

        when(orderRepository.save(any(com.ecommerceapp.orderservice.model.Order.class))).thenReturn(savedOrder);

        com.ecommerceapp.Order.CreateOrderRequest request = com.ecommerceapp.Order.CreateOrderRequest.newBuilder()
                .setProductId(productId)
                .setUserEmail(userEmail)
                .build();

        StreamObserver<com.ecommerceapp.Order.OrderResponse> responseObserver = mock(StreamObserver.class);

        // Act
        orderGrpcServerService.createOrder(request, responseObserver);

        // Assert
        verify(orderRepository).save(orderCaptor.capture());

        com.ecommerceapp.orderservice.model.Order capturedOrder = orderCaptor.getValue();
        assertNotNull(capturedOrder);
        assertEquals(productId, capturedOrder.getProductId());
        assertEquals(userEmail, capturedOrder.getUserEmail());


        ArgumentCaptor<com.ecommerceapp.Order.OrderResponse> responseCaptor = ArgumentCaptor.forClass(com.ecommerceapp.Order.OrderResponse.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        com.ecommerceapp.Order.OrderResponse response = responseCaptor.getValue();
        assertNotNull(response);
        assertEquals(savedOrder.getId(), response.getId());
        assertEquals(savedOrder.getProductId(), response.getProductId());
        assertEquals(savedOrder.getUserEmail(), response.getUserEmail());
        assertEquals(savedOrder.getCreatedAt().toEpochSecond(ZoneOffset.UTC), response.getCreatedAt().getSeconds());
        assertEquals(savedOrder.getCreatedAt().getNano(), response.getCreatedAt().getNanos());
    }


    @Captor
    private ArgumentCaptor<Integer> orderIdCaptor;

    @Test
    public void getOrder_ExistingOrderId_ReturnsResponse() {
        // Arrange
        int orderId = 123;
        int productId = 456;
        String userEmail = "test@example.com";
        LocalDateTime createdAt = LocalDateTime.now();

        com.ecommerceapp.orderservice.model.Order order = new com.ecommerceapp.orderservice.model.Order(productId, userEmail, createdAt);
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        com.ecommerceapp.Order.GetOrderRequest request = com.ecommerceapp.Order.GetOrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();

        StreamObserver<com.ecommerceapp.Order.OrderResponse> responseObserver = mock(StreamObserver.class);

        // Act
        orderGrpcServerService.getOrder(request, responseObserver);

        // Assert
        verify(orderRepository).findById(orderIdCaptor.capture());
        int capturedOrderId = orderIdCaptor.getValue();
        assertEquals(orderId, capturedOrderId);

        ArgumentCaptor<com.ecommerceapp.Order.OrderResponse> responseCaptor = ArgumentCaptor.forClass(com.ecommerceapp.Order.OrderResponse.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        com.ecommerceapp.Order.OrderResponse response = responseCaptor.getValue();
        assertNotNull(response);
        assertEquals(order.getId(), response.getId());
        assertEquals(order.getProductId(), response.getProductId());
        assertEquals(order.getUserEmail(), response.getUserEmail());

    }


    @Test
    public void getOrder_NonExistingOrderId_ReturnsError() {

        int orderId = 123;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        com.ecommerceapp.Order.GetOrderRequest request = com.ecommerceapp.Order.GetOrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();

        StreamObserver<com.ecommerceapp.Order.OrderResponse> responseObserver = mock(StreamObserver.class);


        orderGrpcServerService.getOrder(request, responseObserver);


        verify(orderRepository).findById(orderIdCaptor.capture());
        int capturedOrderId = orderIdCaptor.getValue();
        assertEquals(orderId, capturedOrderId);

        verify(responseObserver).onError(argThat((StatusRuntimeException e) ->
                e.getStatus().getCode() == Status.INVALID_ARGUMENT.getCode()));

        verifyNoMoreInteractions(responseObserver);
    }



}
