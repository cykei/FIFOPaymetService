package com.fifo.productservice.client;

import com.fifo.productservice.client.dto.OrderProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service", url="localhost:8081/api/orders")
public interface OrderClient {

    @GetMapping("/products")
    List<OrderProductResponse> findOrderProductsByCreatedAtAfter(@RequestParam String dateTime);
}
