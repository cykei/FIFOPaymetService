package com.cykei.fifopaymentservice.product.service;

import com.cykei.fifopaymentservice.product.dto.PagingProductResponse;
import com.cykei.fifopaymentservice.product.dto.ProductDto;
import com.cykei.fifopaymentservice.product.mapper.ProductMapper;
import com.cykei.fifopaymentservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public PagingProductResponse getProducts(long categoryId, Long cursor, int size) {
        List<ProductDto> products = productRepository.findProductsByCategoryId(categoryId, cursor, size);
        long nextCursor = products.get(products.size()-1).getProductId();

        return new PagingProductResponse(
                products.size(),
                nextCursor+1,
                productMapper.mapToResponses(products)
        );
    }
}
