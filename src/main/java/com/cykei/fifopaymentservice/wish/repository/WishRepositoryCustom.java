package com.cykei.fifopaymentservice.wish.repository;

import com.cykei.fifopaymentservice.wish.Wish;

import java.util.List;

public interface WishRepositoryCustom {
    List<Wish> getWishes(long productId, int size, Long cursor);
}
