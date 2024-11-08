package com.fifo.orderservice.repository;

import com.fifo.orderservice.entity.Payment;
import com.fifo.orderservice.enums.PaymentStatus;
import com.fifo.orderservice.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    void deleteAllByOrderIdIn(List<Long> orderIds);
    List<Payment> findByPaymentTypeAndPaymentStatusAndCreatedAtBefore(PaymentType paymentType, PaymentStatus paymentStatus, LocalDateTime timeout);
}
