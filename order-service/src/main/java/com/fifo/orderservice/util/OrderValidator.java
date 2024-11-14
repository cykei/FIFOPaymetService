package com.fifo.orderservice.util;

import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.service.dto.OrderCreateRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class OrderValidator {

    public static void validateUser(Order order, long userId) {
        if (order.getUserId() != userId) throw new IllegalArgumentException("권한이 없습니다");
    }

    public static boolean validatePrice(long expectedPrice, OrderCreateRequest createRequest) {
        long sourcePrice = createRequest.getTotalPrice();
        if (expectedPrice != sourcePrice) {
            log.warn("[Order 실패] 주문자: {}, 요청가격: {}, 실제가격: {}", createRequest.getUserId(), sourcePrice, expectedPrice);
            return false;
        }
        return true;
    }
    public static void validateOrderStatus(Order order, OrderStatus orderStatus) {
        if (!orderStatus.getCancelEnableStatus().contains(order.getOrderStatus())) {
            log.info("[Order 취소 실패] 주문번호: {}, 주문상태: {}, 변경요청상태: {}", order.getOrderId(), order.getOrderStatus(), orderStatus);
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    public static void isTimeoutForPayment(Order order) {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(10);
        if (order.getCreatedAt().isBefore(timeout)) {
            throw new IllegalArgumentException("결제완료는 주문후 10분내에 완료되어야 합니다.");
        }
    }
}
