package com.cykei.fifopaymentservice.product.controller;

import com.cykei.fifopaymentservice.common.PagingResponse;
import com.cykei.fifopaymentservice.product.dto.ProductDetailResponse;
import com.cykei.fifopaymentservice.product.dto.ProductResponse;
import com.cykei.fifopaymentservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("categories/{categoryId}")
    public PagingResponse<ProductResponse> getProducts(@PathVariable long categoryId, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") int size) {
        return productService.getProducts(categoryId, cursor, size);
    }

    @GetMapping("products/{productId}")
    public ProductDetailResponse getProductDetail(@PathVariable long productId) {
        return productService.getProductDetail(productId);
    }
}
