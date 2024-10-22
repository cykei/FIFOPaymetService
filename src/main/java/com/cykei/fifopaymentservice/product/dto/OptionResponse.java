package com.cykei.fifopaymentservice.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OptionResponse {
    private long id;

    private String optionName;

    private int optionPrice;

    private String optionImage;

    private int productCount;
}
