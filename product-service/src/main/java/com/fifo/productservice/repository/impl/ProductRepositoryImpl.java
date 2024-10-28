package com.fifo.productservice.repository.impl;


import com.fifo.productservice.entity.QProduct;
import com.fifo.productservice.entity.QProductOption;
import com.fifo.productservice.entity.QRelationshipCategoryProduct;
import com.fifo.productservice.repository.ProductRepositoryCustom;
import com.fifo.productservice.repository.dto.ProductDto;
import com.fifo.productservice.repository.dto.ProductOptionDto;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final QRelationshipCategoryProduct relation = new QRelationshipCategoryProduct("relation");
    private static final QProduct product = new QProduct("product");
    private static final QProductOption productOption = new QProductOption("productOption");
    private final JPAQueryFactory queryFactory;
    private final ApplicationContext applicationContext;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory, ApplicationContext applicationContext) {
        this.queryFactory = queryFactory;
        this.applicationContext = applicationContext;
    }

    @Override
    public List<ProductDto> findProductDtosByCategoryId(long categoryId, Long cursor, int size) {
        return queryFactory
                .select(getProductDtoConstructorExpression())
                .from(relation)
                .join(product).on(relation.productId.eq(product.productId))
                .leftJoin(productOption).on(product.productId.eq(productOption.productId))
                .where(eqCategoryId(categoryId),
                        loeCursor(cursor))
                .groupBy(relation.categoryId, product.productId, product.name)
                .orderBy(product.productId.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<ProductOptionDto> findProductOptionsIn(List<Long> optionsIds) {
        return queryFactory.select(Projections.constructor(ProductOptionDto.class,
                        productOption.id,
                        product.productId,
                        product.price,
                        productOption.id,
                        productOption.optionPrice))
                .from(productOption)
                .join(product).on(productOption.productId.eq(product.productId))
                .where(productOption.id.in(optionsIds))
                .fetch();
    }

    @Override
    public List<ProductDto> findProductDtosByProductIdIn(List<Long> productIds) {
        return queryFactory
                .select(getProductDtoConstructorExpression())
                .from(relation)
                .join(product).on(relation.productId.eq(product.productId))
                .leftJoin(productOption).on(product.productId.eq(productOption.productId))
                .where(isInProductIds(productIds))
                .groupBy(relation.categoryId, product.productId, product.name)
                .fetch();
    }

    private ConstructorExpression<ProductDto> getProductDtoConstructorExpression() {
        return Projections.constructor(ProductDto.class,
                product.productId,
                product.name,
                product.image,
                product.price,
                Expressions.stringTemplate(aggregationFunction(), productOption.optionName).as("options")
        );
    }

    private String aggregationFunction() {
        return isTestExecution() ? "STRING_AGG({0}, ',')" : "GROUP_CONCAT({0})";
    }

    private boolean isTestExecution() {
        return applicationContext.getEnvironment().getProperty("spring.test.context.active") != null;
    }

    private BooleanExpression eqCategoryId(long categoryId) {
        return relation.categoryId.eq(categoryId);
    }

    private BooleanExpression loeCursor(Long cursor) {
        if (Objects.isNull(cursor)) {
            return null;
        }
        return product.productId.loe(cursor);
    }

    private BooleanExpression isInProductIds(List<Long> productIds) {
        return product.productId.in(productIds);
    }
}
