package com.cykei.fifopaymentservice.wish.controller;

import com.cykei.fifopaymentservice.common.UserId;
import com.cykei.fifopaymentservice.wish.dto.PagingWishResponse;
import com.cykei.fifopaymentservice.wish.dto.WishCreateRequest;
import com.cykei.fifopaymentservice.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;

    @GetMapping
    public PagingWishResponse getWishes(
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
}
