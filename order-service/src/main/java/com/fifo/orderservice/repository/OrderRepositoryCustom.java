package com.fifo.orderservice.repository;

import com.fifo.orderservice.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> getOrders(long userId, int size, Long cursor);
}
