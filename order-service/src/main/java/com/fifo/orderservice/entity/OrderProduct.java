package com.fifo.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id", nullable = false)
    private long orderProductId;

    private long orderId;
    private long productId;
    private long optionId;
    private int productCount;
    private int finalPrice; // product + option 가격

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public OrderProduct(long orderId, long productId, long optionId, int productCount, int finalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.optionId = optionId;
        this.productCount = productCount;
        this.finalPrice = finalPrice;
    }

}
