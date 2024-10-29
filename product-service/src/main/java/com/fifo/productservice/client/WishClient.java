package com.fifo.productservice.client;


import com.fifo.productservice.client.dto.WishResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "wish-service")
public interface WishClient {
    @GetMapping("/api/wishes")
    List<WishResponse> findWishesByCreatedAtAfter(@RequestParam("dateTime") String dateTime);
}
