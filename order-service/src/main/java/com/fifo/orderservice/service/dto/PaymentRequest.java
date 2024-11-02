package com.fifo.orderservice.service.dto;

import com.fifo.orderservice.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    @NotNull
    private Long orderId;

    private long paymentPrice;

    private PaymentType paymentType;
}
