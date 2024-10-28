package com.fifo.wishservice.service;

import com.fifo.common.dto.PagingResponse;
import com.fifo.wishservice.client.ProductClient;
import com.fifo.wishservice.client.dto.ProductResponse;
import com.fifo.wishservice.service.dto.WishCreateRequest;
import com.fifo.wishservice.service.dto.WishProductResponse;
import com.fifo.wishservice.entity.Wish;
import com.fifo.wishservice.mapper.WishMapper;
import com.fifo.wishservice.repository.WishRepository;
import com.fifo.wishservice.service.dto.WishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final WishMapper wishMapper;
    private final ProductClient productClient;

    public PagingResponse<WishProductResponse> getWishes(long userId, int size, Long cursor) {
        List<Wish> wishes = wishRepository.getWishes(userId, size, cursor);

        List<Long> productIds = wishes.stream()
                .map(Wish::getProductId)
                .collect(Collectors.toList());
        List<ProductResponse> products = productClient.getProducts(productIds);
        List<WishProductResponse> productResponses = wishMapper.toWishProductResponses(wishes, products);

        Long nextCursor = null;
        if (wishes.size() == size) {
            nextCursor = wishes.get(wishes.size() - 1).getWishId();
        }

        return new PagingResponse<>(
                wishes.size(),
                nextCursor,
                null
                //productResponses
        );
    }

    @Transactional
    public Long createWish(WishCreateRequest wishRequest) {
        Wish wish = wishMapper.toEntity(wishRequest);

        if (wishRepository.existsByProductIdAndUserId(wish.getProductId(), wish.getUserId()))
            return 0L;

        return wishRepository.save(wish).getWishId();
    }

    @Transactional
    public Long deleteWish(long userId, long wishId) {
        Wish wish = wishRepository.findByUserIdAndWishId(userId, wishId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
        wishRepository.delete(wish);
        return wish.getWishId();
    }

    public List<WishResponse> findWishesByCreatedAtAfter(LocalDateTime dateTime) {
        List<Wish> wishes = wishRepository.findByCreatedAtAfter(dateTime);
        return wishMapper.toResponses(wishes);
    }
}
