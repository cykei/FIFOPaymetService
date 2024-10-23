package com.cykei.fifopaymentservice.order;

import com.cykei.fifopaymentservice.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
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
    private OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Order(long userId, String orderAddress, long orderTotalPrice) {
        this.userId = userId;
        this.orderAddress = orderAddress;
        this.orderTotalPrice = orderTotalPrice;
    }
}
