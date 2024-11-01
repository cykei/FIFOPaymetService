package com.fifo.orderservice.client;

import com.fifo.orderservice.client.dto.ProductOptionResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/errorful/case1")
    ResponseEntity<String> case1();

    @CircuitBreaker(name = "test", fallbackMethod = "fallBack")
    @GetMapping("/errorful/case2")
    ResponseEntity<String> case2();

    @CircuitBreaker(name = "test", fallbackMethod = "fallBack")
    @GetMapping("/errorful/case3")
    ResponseEntity<String> case3();

    default ResponseEntity<String> fallBack(Exception e) {
        return ResponseEntity.ok("Breaker blocked api ");
    }
}
