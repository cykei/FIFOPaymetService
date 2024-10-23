package com.cykei.fifopaymentservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagingOrderResponse {
    private long totalCount;
    private Long cursor;
    private List<OrderResponse> orderResponses;
}
