package com.fifo.orderservice.service;

import com.fifo.orderservice.client.ProductClient;
import com.fifo.orderservice.client.dto.ProductOptionResponse;
import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.entity.Payment;
import com.fifo.orderservice.enums.PaymentType;
import com.fifo.orderservice.repository.OrderProductRepository;
import com.fifo.orderservice.repository.OrderRepository;
import com.fifo.orderservice.repository.PaymentRepository;
import com.fifo.orderservice.service.dto.OrderCreateRequest;
import com.fifo.orderservice.service.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    private ProductClient productClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    @InjectMocks
    private OrderService orderService; // 실제 테스트할 서비스 클래스

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_successfulOrderCreation() throws InterruptedException {
        // Given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setUserId(1L);
        orderCreateRequest.setTotalPrice(2200);
        orderCreateRequest.setPaymentType(PaymentType.CREDIT_CARD);

        // 주문 요청 생성
        List<OrderRequest> orderRequests = new ArrayList<>();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOptionId(1L);
        orderRequest.setCount(2);
        orderRequests.add(orderRequest);
        orderCreateRequest.setOrderRequests(orderRequests);

        // 상품 옵션 응답 설정
        ProductOptionResponse productOptionResponse = new ProductOptionResponse(1L, 1L, 1000, 1L, 100, 2);
        List<ProductOptionResponse> productOptions = Collections.singletonList(productOptionResponse);

        // Mock 설정
        when(productClient.findProductOptionsIn(anySet())).thenReturn(productOptions);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(5, 1, TimeUnit.SECONDS)).thenReturn(true);
        when(productClient.decreaseStock(anyLong(), anyInt())).thenReturn(ResponseEntity.ok(true));

        Order savedOrder = new Order(1L, "address", 1000, PaymentType.CREDIT_CARD);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        ResponseEntity<Long> response = orderService.createOrder(orderCreateRequest);

        // Then
        assertNotNull(response);

        // Verify interactions
        verify(productClient, times(1)).findProductOptionsIn(anySet());
        verify(productClient, times(1)).decreaseStock(anyLong(), anyInt());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderProductRepository, times(1)).saveAll(anyList());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(lock, times(1)).unlock();
    }

    @Test
    void createOrder_failedToLock() throws InterruptedException {
        // Given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setUserId(1L);
        orderCreateRequest.setPaymentType(PaymentType.CREDIT_CARD);

        // 주문 요청 생성
        List<OrderRequest> orderRequests = new ArrayList<>();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOptionId(1L);
        orderRequest.setCount(2);
        orderRequests.add(orderRequest);
        orderCreateRequest.setOrderRequests(orderRequests);

        // 상품 옵션 응답 설정
        ProductOptionResponse productOptionResponse = new ProductOptionResponse(1L, 1L, 1000, 1L, 100, 2);
        List<ProductOptionResponse> productOptions = Collections.singletonList(productOptionResponse);


        // Mock 설정
        when(productClient.findProductOptionsIn(anySet())).thenReturn(productOptions);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(5, 1, TimeUnit.SECONDS)).thenReturn(false); // 락 획득 실패 시뮬레이션

        // When / Then
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderCreateRequest);
        });

        // Verify
        verify(lock, never()).unlock();
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_stockFailureAndRestoration() throws InterruptedException {
        // Given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setUserId(1L);
        orderCreateRequest.setPaymentType(PaymentType.CREDIT_CARD);
        orderCreateRequest.setTotalPrice(3700);

        // 주문 요청 생성
        List<OrderRequest> orderRequests = new ArrayList<>();
        OrderRequest orderRequest1 = new OrderRequest();
        orderRequest1.setOptionId(1L);
        orderRequest1.setCount(2);
        OrderRequest orderRequest2 = new OrderRequest();
        orderRequest2.setOptionId(2L);
        orderRequest2.setCount(1);
        orderRequests.add(orderRequest1);
        orderRequests.add(orderRequest2);
        orderCreateRequest.setOrderRequests(orderRequests);

        // 상품 옵션 응답 설정
        ProductOptionResponse productOptionResponse1 = new ProductOptionResponse();
        productOptionResponse1.setOptionId(1L);
        productOptionResponse1.setProductPrice(1000);
        productOptionResponse1.setOptionPrice(100);
        ProductOptionResponse productOptionResponse2 = new ProductOptionResponse();
        productOptionResponse2.setOptionId(2L);
        productOptionResponse2.setProductPrice(1500);
        productOptionResponse2.setOptionPrice(0);
        List<ProductOptionResponse> productOptions = Arrays.asList(productOptionResponse1, productOptionResponse2);

        // Mock 설정
        when(productClient.findProductOptionsIn(anySet())).thenReturn(productOptions);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(lock.tryLock(5, 1, TimeUnit.SECONDS)).thenReturn(true);

        // 첫 번째 재고는 성공적으로 감소, 두 번째 재고는 재고 부족으로 실패
        when(productClient.decreaseStock(1L, 2)).thenReturn(ResponseEntity.ok(true));
        when(productClient.decreaseStock(2L, 1)).thenReturn(ResponseEntity.ok(false)); // 재고 부족

        // When / Then
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderCreateRequest);
        });

        // Verify
        // 첫 번째 재고 감소는 성공
        verify(productClient, times(1)).decreaseStock(1L, 2);
        // 두 번째 재고 감소는 실패
        verify(productClient, times(1)).decreaseStock(2L, 1);
        // 첫 번째 재고는 복원되어야 함
        verify(productClient, times(1)).increaseStock(1L, 2);
        // 두 번째 재고는 복원되지 않음 (감소하지 않았으므로)
        verify(productClient, never()).increaseStock(2L, 1);

        // 락 해제 검증
        verify(lock, times(2)).unlock();
    }
}
