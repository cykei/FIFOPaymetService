package com.cykei.fifopaymentservice.wish.repository;

import com.cykei.fifopaymentservice.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {
    boolean existsByProductIdAndUserId(long productId, long userId);

    Optional<Wish> findByUserIdAndWishId(long userId, long wishId);

    List<Wish> findByCreatedAtAfter(LocalDateTime date);
}
