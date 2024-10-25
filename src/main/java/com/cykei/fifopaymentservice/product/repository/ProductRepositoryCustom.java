package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.repository.dto.ProductDto;
import com.cykei.fifopaymentservice.product.repository.dto.ProductOptionDto;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDto> findProductDtosByCategoryId(long categoryId, Long cursor, int size);
    List<ProductOptionDto> findProductOptionsIn(List<Long> optionsIds);
    List<ProductDto> findProductDtosByProductIdIn(List<Long> productIds);
}
