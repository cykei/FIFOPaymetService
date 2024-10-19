package com.cykei.fifopaymentservice.product.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProductDto {
    private long productId;
    private String name;
    private String image;
    private long price;
    private String options;

    public ProductDto(long productId, String name, String image, long price, String options) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.options = options;
    }

    public String[] getOptions() {
        return options.split(",");
    }
}
