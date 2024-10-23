package com.cykei.fifopaymentservice.order.repository;

import com.cykei.fifopaymentservice.order.Order;
import com.cykei.fifopaymentservice.order.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Objects;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private static final QOrder order = new QOrder("order");
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Order> getOrders(long userId, int size, Long cursor) {
        return queryFactory.selectFrom(order)
                .where(
                        eqUserId(userId),
                        ltOrderId(cursor)
                )
                .orderBy(order.orderId.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression eqUserId(long userId) {
        return order.userId.eq(userId);
    }

    private BooleanExpression ltOrderId(Long orderId) {
        if (Objects.isNull(orderId)) {
            return null;
        }

        return order.orderId.lt(orderId);
    }
}
