package com.fifo.productservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockResponse {
    private Long productOptionId;
    private Integer productCount;
}
