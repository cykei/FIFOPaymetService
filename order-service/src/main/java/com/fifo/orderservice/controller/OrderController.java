package com.fifo.orderservice.controller;

import com.fifo.common.dto.PagingResponse;
import com.fifo.orderservice.config.UserId;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.service.OrderService;
import com.fifo.orderservice.service.dto.OrderCreateRequest;
import com.fifo.orderservice.service.dto.OrderDetailResponse;
import com.fifo.orderservice.service.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public PagingResponse<OrderResponse> getOrders(@RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") int size, @UserId Long userId) {
        return orderService.getOrders(cursor, size, userId);
    }

    @GetMapping("/{orderId}")
    public List<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId, @UserId Long userId) {
        return orderService.getOrderDetail(orderId, userId);
    }

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest, @UserId Long userId) {
        orderCreateRequest.setUserId(userId);
        return orderService.createOrder(orderCreateRequest);
    }

    @DeleteMapping("/{orderId}/status/{orderStatus}")
    public String cancelOrder(@PathVariable Long orderId, @PathVariable OrderStatus orderStatus, @UserId Long userId) {
        return orderService.cancelOrder(orderId, orderStatus, userId);
    }

    @GetMapping("/products")
    public List<OrderDetailResponse> findOrderProductsByCreatedAtAfter(@RequestParam @DateTimeFormat(pattern = "yyyyMMddHHmmss") LocalDateTime dateTime) {
        return orderService.findOrderProductsByCreatedAtAfter(dateTime);
    }

}
