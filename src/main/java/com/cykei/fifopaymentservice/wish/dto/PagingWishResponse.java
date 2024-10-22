package com.cykei.fifopaymentservice.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagingWishResponse {
    private long totalCount;
    private Long cursor;
    private List<WishProductResponse> wishProductResponses;
}
