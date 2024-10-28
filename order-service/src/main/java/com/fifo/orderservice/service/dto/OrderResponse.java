package com.fifo.orderservice.service.dto;

import com.fifo.orderservice.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private long orderId;
    private String orderAddress;
    private long orderTotalPrice; // 한번에 주문한 상품의 총 가격
    private OrderStatus orderStatus;
}
