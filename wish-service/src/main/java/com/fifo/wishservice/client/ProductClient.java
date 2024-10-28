package com.fifo.wishservice.client;

import com.fifo.wishservice.client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-service", url="localhost:8080/api/products")
public interface ProductClient {

    @GetMapping
    List<ProductResponse> getProducts(List<Long> productIds);
}
