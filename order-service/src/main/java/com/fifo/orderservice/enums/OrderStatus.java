package com.fifo.orderservice.enums;

import java.util.Collections;
import java.util.List;

public enum OrderStatus {
    ORDER_CANCEL("주문취소", Collections.emptyList()),
    ORDER_COMPLETE("주문완료", List.of(ORDER_CANCEL)),
    DELIVERY_NOW("배송중", Collections.emptyList()),
    RETURN_REQUEST("반품요청 접수", Collections.emptyList()),
    DELIVERY_COMPLETE("배송완료", List.of(RETURN_REQUEST)),
    RETURN_COMPLETE("반품완료",Collections.emptyList());

    private final String description;
    private final List<OrderStatus> cancelEnableStatus;

    OrderStatus(String description , List<OrderStatus> cancelEnableStatus) {
        this.description = description;
        this.cancelEnableStatus = cancelEnableStatus;
    }

    public String getDescription() {
        return description;
    }

    public List<OrderStatus> getCancelEnableStatus() {
        return cancelEnableStatus;
    }
}
