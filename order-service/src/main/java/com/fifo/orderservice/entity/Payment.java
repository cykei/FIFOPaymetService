package com.fifo.orderservice.entity;

import com.fifo.orderservice.enums.PaymentStatus;
import com.fifo.orderservice.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private long paymentId;

    private long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private Long paymentPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.WAIT;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Payment(long orderId, PaymentType paymentType) {
        this.orderId = orderId;
        this.paymentType = paymentType;
    }

    public void updatePayment(Long paymentPrice) {
        this.paymentPrice = paymentPrice;
        this.paymentStatus = PaymentStatus.COMPLETE;
    }
}
