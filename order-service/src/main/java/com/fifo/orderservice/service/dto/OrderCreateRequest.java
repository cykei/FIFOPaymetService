package com.fifo.orderservice.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {
    String orderAddress;
    Long userId;

    @NotBlank
    List<OrderRequest> orderRequests;
    long totalPrice;
}
