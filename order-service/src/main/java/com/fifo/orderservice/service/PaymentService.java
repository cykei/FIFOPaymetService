package com.fifo.orderservice.service;

import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.entity.Payment;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.enums.PaymentStatus;
import com.fifo.orderservice.enums.PaymentType;
import com.fifo.orderservice.repository.OrderRepository;
import com.fifo.orderservice.repository.PaymentRepository;
import com.fifo.orderservice.service.dto.PaymentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ResponseEntity<String> processPayment(PaymentRequest request) {
        Payment payment = paymentRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("결제는 주문후 30분이내 완료 되어야 합니다."));

        payment.updatePayment(request.getPaymentPrice());
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow();
        if (order.getOrderStatus().equals(OrderStatus.PAYMENT_REQUIRED)) {
            // 뭔가 .. 결제한 금액이 주문한 금액과 맞는지 검증하는 작업이 필요한가? 안맞으면 어떻게 해야되는거지??
            order.updateOrderStatus(OrderStatus.ORDER_COMPLETE);
        } else {
            // 한번 결제가 됐는데 다시한번 결제를 함
            // 환불처리 ...?
        }
        return ResponseEntity.ok("주문완료 되었습니다.");
    }

    @Scheduled(cron = "0 5 * * * *")
    public void removeOrderWIthOutCash() {
        // 무통장 입금 방식 외 입금은 5분마다 체크해서 이탈로 감지되는 row 는 삭제한다.
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(30);
        List<Order> orders = orderRepository.findByOrderStatusAndCreatedAtBefore(OrderStatus.PAYMENT_REQUIRED, timeout);
        log.info("주문이탈: " + orders.size());
        orderRepository.deleteAll(orders);
        List<Long> orderIds = orders.stream().map(Order::getOrderId).toList();
        paymentRepository.deleteAllByOrderIdIn(orderIds);
    }

    @Scheduled(cron =  "0 0 10 * * *")
    public void removeOrderWIthCash() {
        // 무통장 입금의 경우 주문 이후 2일후 아침 10시 까지 입금되지 않은 경우 삭제한다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeout = LocalDateTime.of(now.toLocalDate(), LocalTime.of(23,59));
        List<Payment> payments = paymentRepository.findByPaymentTypeAndPaymentStatusAndCreatedAtBefore(
                PaymentType.CASH, PaymentStatus.WAIT, timeout);
        List<Long> orderIds = payments.stream().map(Payment::getOrderId).toList();
        paymentRepository.deleteAll(payments);
        orderRepository.deleteAllById(orderIds);
    }
}
