package com.cykei.fifopaymentservice.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {
    String orderAddress;

    @NotBlank
    List<OrderRequest> orderRequests;
    long totalPrice;
}
