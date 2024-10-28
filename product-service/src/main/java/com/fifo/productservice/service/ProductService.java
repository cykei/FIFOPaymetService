package com.fifo.productservice.service;

import com.fifo.common.dto.PagingResponse;
import com.fifo.productservice.entity.Product;
import com.fifo.productservice.mapper.ProductMapper;
import com.fifo.productservice.repository.ProductOptionRepository;
import com.fifo.productservice.repository.ProductRepository;
import com.fifo.productservice.repository.dto.ProductDto;
import com.fifo.productservice.repository.dto.ProductOptionDto;
import com.fifo.productservice.service.dto.ProductDetailResponse;
import com.fifo.productservice.service.dto.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductMapper productMapper;

    public PagingResponse<ProductResponse> getProducts(long categoryId, Long cursor, int size) {
        List<ProductDto> products = productRepository.findProductDtosByCategoryId(categoryId, cursor, size);
        Long nextCursor = null;
        if (products.size() == size) {
            nextCursor = products.get(products.size() - 1).getProductId();
        }
        return new PagingResponse<> (
                products.size(),
                nextCursor,
                productMapper.toResponses(products)
        );
    }

    public ProductDetailResponse getProductDetail(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
        return productMapper.toDetailResponse(product);
    }

    public List<ProductResponse> getProducts(List<Long> productIds) {
        List<ProductDto> products = productRepository.findProductDtosByProductIdIn(productIds);
        return productMapper.toResponses(products);
    }

    public List<ProductOptionDto> findProductOptionsIn(List<Long> optionIds){
        return productRepository.findProductOptionsIn(optionIds);
    }

    @Transactional
    public void decreaseStock(Long optionId, Integer quantity){
        productOptionRepository.decreaseStock(optionId, quantity);
    }

    @Transactional
    public void increaseStock(Long optionId, Integer quantity){
        productOptionRepository.increaseStock(optionId, quantity);
    }
}
