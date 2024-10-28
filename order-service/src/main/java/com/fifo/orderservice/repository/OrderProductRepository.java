package com.fifo.orderservice.repository;

import com.fifo.orderservice.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(long orderId);
    List<OrderProduct> findByCreatedAtAfter(LocalDateTime localDateTime);
}
