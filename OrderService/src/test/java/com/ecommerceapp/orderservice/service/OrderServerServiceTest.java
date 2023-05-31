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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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


        orderGrpcServerService.createOrder(request, responseObserver);


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


        orderGrpcServerService.getOrder(request, responseObserver);


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


    @Captor
    private ArgumentCaptor<String> userEmailCaptor;
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
                e.getStatus().getCode() == Status.NOT_FOUND.getCode()));

        verifyNoMoreInteractions(responseObserver);
    }

    @Test
    public void getAllOrdersOfUser_NonExistingUserEmail_ReturnsNotFoundError() {

        String userEmail = "test@example.com";

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(Collections.emptyList());

        com.ecommerceapp.Order.GetAllOrdersOfUserRequest request = com.ecommerceapp.Order.GetAllOrdersOfUserRequest.newBuilder()
                .setUserEmail(userEmail)
                .build();

        StreamObserver<com.ecommerceapp.Order.GetAllOrdersOfUserResponse> responseObserver = mock(StreamObserver.class);


        orderGrpcServerService.getAllOrdersOfUser(request, responseObserver);


        verify(orderRepository).findByUserEmail(userEmailCaptor.capture());
        String capturedUserEmail = userEmailCaptor.getValue();
        assertEquals(userEmail, capturedUserEmail);

        verify(responseObserver).onError(argThat((StatusRuntimeException e) ->
                e.getStatus().getCode() == Status.NOT_FOUND.getCode()));

        verifyNoMoreInteractions(responseObserver);
    }

    @Test
    public void getAllOrdersOfUser_ExistingUserEmail_ReturnsResponseWithOrders() {

        String userEmail = "test@example.com";
        List<com.ecommerceapp.orderservice.model.Order> orders = new ArrayList<>();
        orders.add(new com.ecommerceapp.orderservice.model.Order(1,123, userEmail, LocalDateTime.now()));
        orders.add(new com.ecommerceapp.orderservice.model.Order(2,456, userEmail, LocalDateTime.now()));

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(orders);

        com.ecommerceapp.Order.GetAllOrdersOfUserRequest request = com.ecommerceapp.Order.GetAllOrdersOfUserRequest.newBuilder()
                .setUserEmail(userEmail)
                .build();

        StreamObserver<com.ecommerceapp.Order.GetAllOrdersOfUserResponse> responseObserver = mock(StreamObserver.class);


        orderGrpcServerService.getAllOrdersOfUser(request, responseObserver);


        verify(orderRepository).findByUserEmail(userEmailCaptor.capture());
        String capturedUserEmail = userEmailCaptor.getValue();
        assertEquals(userEmail, capturedUserEmail);

        ArgumentCaptor<com.ecommerceapp.Order.GetAllOrdersOfUserResponse> responseCaptor = ArgumentCaptor.forClass(com.ecommerceapp.Order.GetAllOrdersOfUserResponse.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        com.ecommerceapp.Order.GetAllOrdersOfUserResponse response = responseCaptor.getValue();
        assertNotNull(response);
        assertEquals(orders.size(), response.getOrdersList().size());

    }
    @Test
    public void getAllOrdersOfUser_ExceptionOccurs_ReturnsUnknownError() {

        String userEmail = "test@example.com";

        when(orderRepository.findByUserEmail(userEmail)).thenThrow(new RuntimeException("Some error occurred"));

        com.ecommerceapp.Order.GetAllOrdersOfUserRequest request = com.ecommerceapp.Order.GetAllOrdersOfUserRequest.newBuilder()
                .setUserEmail(userEmail)
                .build();

        StreamObserver<com.ecommerceapp.Order.GetAllOrdersOfUserResponse> responseObserver = mock(StreamObserver.class);


        orderGrpcServerService.getAllOrdersOfUser(request, responseObserver);


        verify(orderRepository).findByUserEmail(userEmailCaptor.capture());
        String capturedUserEmail = userEmailCaptor.getValue();
        assertEquals(userEmail, capturedUserEmail);

        verify(responseObserver).onError(argThat((StatusRuntimeException e) ->
                e.getStatus().getCode() == Status.INTERNAL.getCode()));

        verifyNoMoreInteractions(responseObserver);
    }

    @Test
    public void getAllOrders_ExistingOrders_ReturnsResponseWithOrders() {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1,123, "user1@example.com", LocalDateTime.now()));
        orders.add(new Order(2,456, "user2@example.com", LocalDateTime.now()));

        when(orderRepository.findAll()).thenReturn(orders);

        com.ecommerceapp.Order.GetAllOrdersRequest request = com.ecommerceapp.Order.GetAllOrdersRequest.newBuilder().build();

        StreamObserver<com.ecommerceapp.Order.GetAllOrdersResponse> responseObserver = mock(StreamObserver.class);


        orderGrpcServerService.getAllOrders(request, responseObserver);


        verify(orderRepository).findAll();

        ArgumentCaptor<com.ecommerceapp.Order.GetAllOrdersResponse> responseCaptor = ArgumentCaptor.forClass(com.ecommerceapp.Order.GetAllOrdersResponse.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();

        com.ecommerceapp.Order.GetAllOrdersResponse response = responseCaptor.getValue();
        assertNotNull(response);
        assertEquals(orders.size(), response.getOrdersList().size());

    }

    @Test
    public void getAllOrders_NoOrdersFound_ReturnsNotFoundError() {

        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        com.ecommerceapp.Order.GetAllOrdersRequest request = com.ecommerceapp.Order.GetAllOrdersRequest.newBuilder().build();

        StreamObserver<com.ecommerceapp.Order.GetAllOrdersResponse> responseObserver = mock(StreamObserver.class);

        orderGrpcServerService.getAllOrders(request, responseObserver);

        verify(orderRepository).findAll();

        verify(responseObserver).onError(argThat((StatusRuntimeException e) ->
                e.getStatus().getCode() == Status.NOT_FOUND.getCode()));

        verifyNoMoreInteractions(responseObserver);
    }


    @Test
    public void getAllOrders_ExceptionOccurs_ReturnsUnknownError() {

        when(orderRepository.findAll()).thenThrow(new RuntimeException("Some error occurred"));

        com.ecommerceapp.Order.GetAllOrdersRequest request = com.ecommerceapp.Order.GetAllOrdersRequest.newBuilder().build();

        StreamObserver<com.ecommerceapp.Order.GetAllOrdersResponse> responseObserver = mock(StreamObserver.class);

        orderGrpcServerService.getAllOrders(request, responseObserver);

        verify(orderRepository).findAll();

        verify(responseObserver).onError(argThat((StatusRuntimeException e) ->
                e.getStatus().getCode() == Status.INTERNAL.getCode()));

        verifyNoMoreInteractions(responseObserver);
    }

}
