package com.fifo.orderservice.service;

import com.fifo.orderservice.client.PaymentClient;
import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.entity.OrderProduct;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.repository.OrderProductRepository;
import com.fifo.orderservice.repository.OrderRepository;
import com.fifo.orderservice.service.dto.PaymentRequest;
import com.fifo.orderservice.util.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PaymentClient paymentClient;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<String> processPayment(PaymentRequest request, long userId) {
        long orderId = request.getOrderId();
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderValidator.validateUser(order, userId);
        OrderValidator.isTimeoutForPayment(order);

        // 1.주문진행
        processPurchase(order);
        // 2. 결제
        try {
            paymentClient.payment(orderId, request);
            // 3. 결제완료
            order.updateOrderStatus(OrderStatus.ORDER_COMPLETE);
            orderRepository.save(order);
        } catch (Exception e) {
            log.warn("[결제 실패] : {}", orderId);
            rollbackPurchase(order);
            throw new RuntimeException("결제처리에 실패했습니다.");
        }

        return ResponseEntity.ok("주문완료 되었습니다.");
    }

    private void processPurchase(Order order) {
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getOrderId());
        for (OrderProduct orderProduct : orderProducts) {
            boolean isPurchased = decreaseStock(orderProduct.getOptionId(), orderProduct.getProductCount());
            if (!isPurchased) {
                throw new IllegalArgumentException("재고부족으로 결제가 취소되었습니다.");
            }
        }
    }

    private void rollbackPurchase(Order order) {
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getOrderId());
        for (OrderProduct orderProduct : orderProducts) {
            increaseStock(orderProduct.getOptionId(), orderProduct.getProductCount());
            log.info("[rollback stock] {} : {}", orderProduct.getOptionId(), orderProduct.getProductCount());
        }
    }

    private boolean decreaseStock(long productOptionId, int quantity) {
        String key = String.format("stock:productOption:%d", productOptionId);
        Long stock = redisTemplate.opsForValue().decrement(key, quantity);

        // 2. 재고가 0 이하로 내려갔을 경우
        if (stock != null && stock < 0) {
            // 재고 부족으로 구매 실패, 재고 복구
            increaseStock(productOptionId, quantity);
            return false;
        }
        // 3. stock == null 인경우는??

        // 4. 구매 성공
        return true;
    }

    private void increaseStock(long productOptionId, int quantity) {
        String key = String.format("stock:productOption:%d", productOptionId);
        redisTemplate.opsForValue().increment(key, quantity);
    }
}