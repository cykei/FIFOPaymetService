package com.fifo.orderservice.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    long optionId;
    int count;
}
