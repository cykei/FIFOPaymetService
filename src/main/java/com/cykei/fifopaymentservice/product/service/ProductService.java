package com.cykei.fifopaymentservice.product.service;

import com.cykei.fifopaymentservice.product.Product;
import com.cykei.fifopaymentservice.product.dto.PagingProductResponse;
import com.cykei.fifopaymentservice.product.dto.ProductDetailResponse;
import com.cykei.fifopaymentservice.product.mapper.ProductMapper;
import com.cykei.fifopaymentservice.product.repository.ProductRepository;
import com.cykei.fifopaymentservice.product.repository.dto.ProductDto;
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
        long nextCursor = products.get(products.size() - 1).getProductId();

        return new PagingProductResponse(
                products.size(),
                nextCursor + 1,
                productMapper.toResponses(products)
        );
    }

    public ProductDetailResponse getProductDetail(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
        return productMapper.toDetailResponse(product);
    }

    public List<Product> getProducts(List<Long> productIds) {
        return productRepository.findProductsByProductIdIn(productIds);
    }
}
