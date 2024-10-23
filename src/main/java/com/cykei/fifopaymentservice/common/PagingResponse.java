package com.cykei.fifopaymentservice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagingResponse<T> {
    private long totalCount;
    private Long cursor;
    private List<T> orderResponses;
}
