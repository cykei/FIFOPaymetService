package com.fifo.orderservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductOptionResponse {
    private long id;
    private long productId;
    private int productPrice;
    private long optionId;
    private int optionPrice;
    private int productCount;

    public int getFinalProductPrice() {
        return productPrice + optionPrice;
    }
}
