package com.cykei.fifopaymentservice.order.dto;


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
