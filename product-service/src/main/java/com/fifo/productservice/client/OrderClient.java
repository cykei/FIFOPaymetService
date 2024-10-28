package com.fifo.productservice.client;

import com.fifo.productservice.client.dto.OrderProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "order-service", url="localhost:8080/api/orders")
public interface OrderClient {

    @GetMapping
    List<OrderProductResponse> findOrderProductsByCreatedAtAfter(LocalDateTime dateTime);
}
