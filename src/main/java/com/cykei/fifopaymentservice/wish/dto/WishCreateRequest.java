package com.cykei.fifopaymentservice.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WishCreateRequest {
    long userId;
    long productId;
}
