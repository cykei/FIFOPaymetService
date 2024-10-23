package com.cykei.fifopaymentservice.wish.repository;

import com.cykei.fifopaymentservice.wish.Wish;

import java.util.List;

public interface WishRepositoryCustom {
    List<Wish> getWishes(long userId, int size, Long cursor);
}
