package com.cykei.fifopaymentservice.order.repository;

import com.cykei.fifopaymentservice.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom{
}
