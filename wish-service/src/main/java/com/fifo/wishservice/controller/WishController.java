package com.fifo.wishservice.controller;


import com.fifo.common.dto.PagingResponse;
import com.fifo.wishservice.config.UserId;
import com.fifo.wishservice.service.WishService;
import com.fifo.wishservice.service.dto.WishCreateRequest;
import com.fifo.wishservice.service.dto.WishProductResponse;
import com.fifo.wishservice.service.dto.WishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;

    @GetMapping("/my")
    public PagingResponse<WishProductResponse> getWishes(
            @UserId Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        return wishService.getWishes(userId, size, cursor);
    }

    @PostMapping("/{productId}")
    public Long createWish(@UserId Long userId, @PathVariable long productId) {
        WishCreateRequest wishCreateRequest = new WishCreateRequest(userId, productId);
        return wishService.createWish(wishCreateRequest);
    }

    @DeleteMapping("/{wishId}")
    public Long deleteWish(@UserId Long userId, @PathVariable long wishId) {
        return wishService.deleteWish(userId, wishId);
    }

    // for product-service
    @GetMapping
    List<WishResponse> findWishesByCreatedAtAfter(@RequestParam @DateTimeFormat(pattern = "yyyyMMddHHmmss")  LocalDateTime dateTime){
        return wishService.findWishesByCreatedAtAfter(dateTime);
    }
}
