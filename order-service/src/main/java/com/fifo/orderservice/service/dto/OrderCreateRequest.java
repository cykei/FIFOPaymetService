package com.fifo.orderservice.service.dto;

import com.fifo.orderservice.enums.PaymentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {
    @NotBlank
    String orderAddress;
    Long userId;

    @NotBlank
    List<OrderRequest> orderRequests;
    long totalPrice;

    @NotBlank
    PaymentType paymentType;
}
