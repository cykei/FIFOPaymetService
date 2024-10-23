package com.cykei.fifopaymentservice.order.controller;

import com.cykei.fifopaymentservice.common.PagingResponse;
import com.cykei.fifopaymentservice.common.UserId;
import com.cykei.fifopaymentservice.order.dto.OrderCreateRequest;
import com.cykei.fifopaymentservice.order.dto.OrderDetailResponse;
import com.cykei.fifopaymentservice.order.dto.OrderResponse;
import com.cykei.fifopaymentservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
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
}
