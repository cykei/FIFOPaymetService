package com.fifo.wishservice.repository;


import com.fifo.wishservice.entity.Wish;

import java.util.List;

public interface WishRepositoryCustom {
    List<Wish> getWishes(long userId, int size, Long cursor);
}
