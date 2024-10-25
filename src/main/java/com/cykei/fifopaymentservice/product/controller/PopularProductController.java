package com.cykei.fifopaymentservice.product.controller;

import com.cykei.fifopaymentservice.product.dto.ProductResponse;
import com.cykei.fifopaymentservice.product.service.PopularProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("products/popular")
@RestController
public class PopularProductController {
    private final PopularProductService popularProductService;

    @GetMapping
    public List<ProductResponse> getPopularProducts() {
        return popularProductService.getPopularProducts(10);
    }
}
