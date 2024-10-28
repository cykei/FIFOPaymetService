package com.fifo.orderservice.service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {
    private long orderProductId;
    private long productId;
    private String productName;
    private String mainImage;
    private String optionName;
    private int productCount;
    private int finalPrice;
}
