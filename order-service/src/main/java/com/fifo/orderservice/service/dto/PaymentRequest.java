package com.fifo.orderservice.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    @NotNull
    private Long orderId;
    private String paymentType;
    private String creditCardNumber;
    private String cardHolderName;
}
