package com.fifo.productservice.repository;

import com.fifo.productservice.repository.dto.ProductDto;
import com.fifo.productservice.repository.dto.ProductOptionDto;

import java.util.List;
import java.util.Set;

public interface ProductRepositoryCustom {
    List<ProductDto> findProductDtosByCategoryId(long categoryId, Long cursor, int size);
    List<ProductOptionDto> findProductOptionsIn(Set<Long> optionsIds);
    List<ProductDto> findProductDtosByProductIdIn(List<Long> productIds);
}
