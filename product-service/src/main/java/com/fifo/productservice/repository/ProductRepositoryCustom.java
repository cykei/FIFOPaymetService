package com.fifo.productservice.repository;

import com.fifo.productservice.repository.dto.ProductDto;
import com.fifo.productservice.repository.dto.ProductOptionDto;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDto> findProductDtosByCategoryId(long categoryId, Long cursor, int size);
    List<ProductOptionDto> findProductOptionsIn(List<Long> optionsIds);
    List<ProductDto> findProductDtosByProductIdIn(List<Long> productIds);
}
