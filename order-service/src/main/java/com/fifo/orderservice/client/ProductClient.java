package com.fifo.orderservice.client;

import com.fifo.orderservice.client.dto.ProductOptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/options")
    List<ProductOptionResponse> findProductOptionsIn(@RequestParam("optionIds") List<Long> optionIds);

    @PostMapping("/api/products/decreaseStock")
    void decreaseStock(@RequestParam("optionId") Long optionId, @RequestParam("quantity") Integer quantity);

    @PostMapping("/api/products/increaseStock")
    void increaseStock(@RequestParam("optionId") Long optionId, @RequestParam("quantity") Integer quantity);

}
