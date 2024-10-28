package com.fifo.productservice.controller;

import com.fifo.common.dto.PagingResponse;
import com.fifo.productservice.repository.dto.ProductOptionDto;
import com.fifo.productservice.service.dto.ProductDetailResponse;
import com.fifo.productservice.service.dto.ProductResponse;
import com.fifo.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("categories/{categoryId}")
    public PagingResponse<ProductResponse> getProducts(@PathVariable long categoryId, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") int size) {
        return productService.getProducts(categoryId, cursor, size);
    }

    @GetMapping("{productId}")
    public ProductDetailResponse getProductDetail(@PathVariable long productId) {
        return productService.getProductDetail(productId);
    }

    // for wish-service
    @GetMapping
    public List<ProductResponse> getProducts(List<Long> productIds) {
        return productService.getProducts(productIds);
    }

    // for order-service
    @GetMapping("/options")
    public List<ProductOptionDto> findProductOptionsIn(@RequestParam List<Long> optionIds) {
        return productService.findProductOptionsIn(optionIds);
    }

    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestParam Long optionId, @RequestParam Integer quantity) {
        productService.decreaseStock(optionId, quantity);
    }

    @PostMapping("/increaseStock")
    public void increaseStock(@RequestParam Long optionId, @RequestParam Integer quantity) {
        productService.increaseStock(optionId, quantity);
    }
}
