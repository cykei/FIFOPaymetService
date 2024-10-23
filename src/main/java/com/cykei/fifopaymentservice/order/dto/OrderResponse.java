package com.cykei.fifopaymentservice.order.dto;

import com.cykei.fifopaymentservice.order.enums.OrderStatus;
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
