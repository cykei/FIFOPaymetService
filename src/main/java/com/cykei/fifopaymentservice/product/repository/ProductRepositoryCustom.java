package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.repository.dto.ProductDto;
import com.cykei.fifopaymentservice.product.repository.dto.ProductOptionDto;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDto> findProductsByCategoryId(long categoryId, Long cursor, int size);
    List<ProductOptionDto> findProductOptionsIn(List<Long> optionsIds);
}
