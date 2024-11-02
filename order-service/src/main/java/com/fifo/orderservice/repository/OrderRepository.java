package com.fifo.orderservice.repository;

import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom{
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByOrderStatusAndCreatedAtBefore(OrderStatus orderStatus, LocalDateTime createdAt);
}
