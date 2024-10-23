package com.cykei.fifopaymentservice.order.repository;

import com.cykei.fifopaymentservice.order.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> getOrders(long userId, int size, Long cursor);
}
