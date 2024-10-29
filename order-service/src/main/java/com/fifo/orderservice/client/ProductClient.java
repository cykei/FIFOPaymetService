package com.fifo.orderservice.client;

import com.fifo.orderservice.client.dto.ProductOptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", url = "localhost:8082/api/products")
public interface ProductClient {

    @GetMapping("/options")
    List<ProductOptionResponse> findProductOptionsIn(@RequestParam List<Long> optionIds);

    @PostMapping("/decreaseStock")
    void decreaseStock(@RequestParam Long optionId, @RequestParam Integer quantity);

    @PostMapping("/increaseStock")
    void increaseStock(@RequestParam Long optionId, @RequestParam Integer quantity);

}
