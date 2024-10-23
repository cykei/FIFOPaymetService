package com.cykei.fifopaymentservice.product.repository.impl;

import com.cykei.fifopaymentservice.product.QProductOption;
import com.cykei.fifopaymentservice.product.repository.ProductOptionRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOptionRepositoryImpl implements ProductOptionRepositoryCustom {
    private static final QProductOption productOption = new QProductOption("productOption");
    private final JPAQueryFactory queryFactory;

    public ProductOptionRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void decreaseStock(long optionId, int count) {
        queryFactory.update(productOption)
                .set(productOption.productCount, productOption.productCount.subtract(count))
                .where(productOption.id.eq(optionId))
                .execute();
    }
}
