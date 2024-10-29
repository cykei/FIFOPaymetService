package com.fifo.productservice.client;

import com.fifo.productservice.client.dto.OrderProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderClient {
    @GetMapping("/api/orders/products")
    List<OrderProductResponse> findOrderProductsByCreatedAtAfter(@RequestParam("dateTime") String dateTime);
}
