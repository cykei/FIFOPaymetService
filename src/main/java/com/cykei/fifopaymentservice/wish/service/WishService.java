package com.cykei.fifopaymentservice.wish.service;

import com.cykei.fifopaymentservice.product.Product;
import com.cykei.fifopaymentservice.product.service.ProductService;
import com.cykei.fifopaymentservice.wish.Wish;
import com.cykei.fifopaymentservice.wish.dto.PagingWishResponse;
import com.cykei.fifopaymentservice.wish.dto.WishCreateRequest;
import com.cykei.fifopaymentservice.wish.dto.WishProductResponse;
import com.cykei.fifopaymentservice.wish.mapper.WishMapper;
import com.cykei.fifopaymentservice.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final WishMapper wishMapper;
    private final ProductService productService;

    public PagingWishResponse getWishes(long userId, int size, Long cursor) {
        List<Wish> wishes = wishRepository.getWishes(userId, size, cursor);

        List<Long> productIds = wishes.stream()
                .map(Wish::getProductId)
                .collect(Collectors.toList());
        List<Product> products = productService.getProducts(productIds);
        List<WishProductResponse> productResponses = wishMapper.toResponses(wishes, products);

        Long nextCursor = null;
        if (wishes.size() == size) {
            nextCursor = wishes.get(wishes.size() - 1).getWishId();
        }

        return new PagingWishResponse(
                wishes.size(),
                nextCursor,
                productResponses
        );
    }

    @Transactional
    public Long createWish(WishCreateRequest wishRequest) {
        Wish wish = wishMapper.toEntity(wishRequest);

        if (wishRepository.existsByProductIdAndUserId(wish.getProductId(), wish.getUserId()))
            return 1L;

        return wishRepository.save(wish).getWishId();
    }

    @Transactional
    public Long deleteWish(long userId, long wishId) {
        Wish wish = wishRepository.findByUserIdAndWishId(userId, wishId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품입니다."));
        wishRepository.delete(wish);
        return wish.getWishId();
    }
}
