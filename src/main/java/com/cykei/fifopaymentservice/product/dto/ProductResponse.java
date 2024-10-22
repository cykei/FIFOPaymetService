package com.cykei.fifopaymentservice.product.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private long productId;
    private String name;
    private String mainImage;
    private long price;
    private String[] options;
}
