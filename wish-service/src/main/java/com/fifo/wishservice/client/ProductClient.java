package com.fifo.wishservice.client;

import com.fifo.wishservice.client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products")
    List<ProductResponse> getProducts(@RequestParam("productIds") List<Long> productIds);
}
