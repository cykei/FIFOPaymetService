package com.fifo.productservice.repository.dto;

import lombok.Getter;

@Getter
public class ProductDto {
    private long productId;
    private String name;
    private String image;
    private int price;
    private String options;

    public ProductDto(long productId, String name, String image, int price, String options) {
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
