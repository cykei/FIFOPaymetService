package com.fifo.orderservice.enums;

public enum OrderStatus {
    ORDER_CANCEL("주문취소"),
    ORDER_COMPLETE("주문완료"),
    DELIVERY_NOW("배송중"),
    DELIVERY_COMPLETE("배송완료"),
    RETURN_REQUEST("반품요청 접수"),
    RETURN_COMPLETE("반품완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
