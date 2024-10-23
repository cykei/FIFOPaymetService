package com.cykei.fifopaymentservice.order.repository;

import com.cykei.fifopaymentservice.order.Order;
import com.cykei.fifopaymentservice.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom{
    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
