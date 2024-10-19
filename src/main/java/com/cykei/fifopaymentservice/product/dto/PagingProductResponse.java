package com.cykei.fifopaymentservice.product.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PagingProductResponse (
    long totalCount,
    @Nullable long cursor,
    @NotNull List<ProductResponse> products
) {}
