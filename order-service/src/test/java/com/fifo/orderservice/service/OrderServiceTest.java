package com.fifo.orderservice.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    void createOrder() {
        // 1. createOrderRequest 검증 (보안)
        // 2. orderRequest 로 받은 물건들 락 획득 및 재고 차감
        // 3. 주문생성.
    }
}