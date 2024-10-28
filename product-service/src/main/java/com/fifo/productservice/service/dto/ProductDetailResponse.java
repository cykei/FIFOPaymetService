package com.fifo.productservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductDetailResponse {
    private long productId;
    private String name;
    private String image;
    private int price;
    private List<OptionResponse> optionResponses;
}
