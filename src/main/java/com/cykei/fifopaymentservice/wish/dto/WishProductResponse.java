package com.cykei.fifopaymentservice.wish.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishProductResponse {
    private long wishId;
    private long productId;
    private String name;
    private String mainImage;
    private long price;
    private String[] options;
}
