package com.fifo.orderservice.entity;

import com.fifo.orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private long orderId;

    private long userId;

    private String orderAddress;

    private long orderTotalPrice; // 한번에 주문한 상품의 총 가격

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PAYMENT_REQUIRED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Order(long userId, String orderAddress, long orderTotalPrice) {
        this.userId = userId;
        this.orderAddress = orderAddress;
        this.orderTotalPrice = orderTotalPrice;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
