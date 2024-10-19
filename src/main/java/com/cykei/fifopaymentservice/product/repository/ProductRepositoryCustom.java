package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.dto.ProductDto;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDto> findProductsByCategoryId(long categoryId, Long cursor, int size);
}
