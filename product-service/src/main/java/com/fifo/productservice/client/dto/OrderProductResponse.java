package com.fifo.productservice.client.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductResponse {
    private long orderProductId;
    private long productId;
    private int productCount;
}
