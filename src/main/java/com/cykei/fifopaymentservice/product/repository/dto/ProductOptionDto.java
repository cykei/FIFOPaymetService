package com.cykei.fifopaymentservice.product.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductOptionDto {
    private long id;
    private long productId;
    private int productPrice;
    private long optionId;
    private int optionPrice;

    public int getFinalProductPrice() {
        return productPrice + optionPrice;
    }
}
