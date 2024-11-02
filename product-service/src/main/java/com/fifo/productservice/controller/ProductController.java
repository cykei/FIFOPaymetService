package com.fifo.productservice.controller;

import com.fifo.common.dto.PagingResponse;
import com.fifo.productservice.repository.dto.ProductOptionDto;
import com.fifo.productservice.service.ProductService;
import com.fifo.productservice.service.dto.ProductDetailResponse;
import com.fifo.productservice.service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/api/products")
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
    public List<ProductResponse> getProducts(@RequestParam List<Long> productIds) {
        return productService.getProducts(productIds);
    }

    // for order-service
    @GetMapping("/options")
    public List<ProductOptionDto> findProductOptionsIn(@RequestParam Set<Long> optionIds) {
        return productService.findProductOptionsIn(optionIds);
    }

    @PostMapping("/decreaseStock")
    public ResponseEntity<Boolean> decreaseStock(@RequestParam Long optionId, @RequestParam Integer quantity) {
        return productService.decreaseStock(optionId, quantity);
    }

    @PostMapping("/increaseStock")
    public ResponseEntity<Boolean> increaseStock(@RequestParam Long optionId, @RequestParam Integer quantity) {
        return productService.increaseStock(optionId, quantity);
    }
}
