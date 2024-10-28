package com.fifo.wishservice.repository;

import com.fifo.wishservice.entity.QWish;
import com.fifo.wishservice.entity.Wish;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class WishRepositoryImpl implements WishRepositoryCustom {

    private static final QWish wish = new QWish("wish");
    private final JPAQueryFactory queryFactory;

    public WishRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Wish> getWishes(long userId, int size, Long cursor) {
        return queryFactory.selectFrom(wish)
                .where(
                        eqUserId(userId),
                        ltWishId(cursor)
                )
                .orderBy(wish.wishId.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression eqUserId(long userId) {
        return wish.userId.eq(userId);
    }

    private BooleanExpression ltWishId(Long wishId) {
        if (Objects.isNull(wishId)) {
            return null;
        }

        return wish.wishId.lt(wishId);
    }

}
