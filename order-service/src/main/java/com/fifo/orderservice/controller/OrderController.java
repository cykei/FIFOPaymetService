package com.fifo.orderservice.controller;


import com.fifo.common.dto.PagingResponse;
import com.fifo.orderservice.service.dto.OrderCreateRequest;
import com.fifo.orderservice.service.dto.OrderDetailResponse;
import com.fifo.orderservice.service.dto.OrderResponse;
import com.fifo.orderservice.service.OrderService;
import com.fifo.userservice.config.UserId;
import lombok.RequiredArgsConstructor;
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
    public Long createOrder(@RequestBody OrderCreateRequest orderCreateRequest, @UserId Long userId) {
        return orderService.createOrder(orderCreateRequest, userId);
    }

    @DeleteMapping("/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, @UserId Long userId) {
        return orderService.cancelOrder(orderId, userId);
    }

    @GetMapping("/products")
    public List<OrderDetailResponse> findOrderProductsByCreatedAtAfter(@RequestParam LocalDateTime dateTime) {
        return orderService.findOrderProductsByCreatedAtAfter(dateTime);
    }
}
