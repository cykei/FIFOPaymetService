package com.cykei.fifopaymentservice.product.repository.impl;

import com.cykei.fifopaymentservice.product.QProduct;
import com.cykei.fifopaymentservice.product.QProductOption;
import com.cykei.fifopaymentservice.product.QRelationshipCategoryProduct;
import com.cykei.fifopaymentservice.product.dto.ProductDto;
import com.cykei.fifopaymentservice.product.repository.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final QRelationshipCategoryProduct relation = new QRelationshipCategoryProduct("relation");
    private static final QProduct product = new QProduct("product");
    private static final QProductOption productOption = new QProductOption("productOption");
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    public ProductRepositoryImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public List<ProductDto> findProductsByCategoryId(long categoryId, Long cursor, int size) {
        return queryFactory
                .select(Projections.constructor(ProductDto.class,
                        product.productId,
                        product.name,
                        product.image,
                        product.price,
                        Expressions.stringTemplate(aggregationFunction(), productOption.optionName).as("options")
                ))
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

    private String aggregationFunction() {
        boolean isH2 = entityManager.getEntityManagerFactory().getProperties()
                .get("hibernate.dialect").toString().contains("H2Dialect");

        return isH2 ? "STRING_AGG({0}, ',')" : "GROUP_CONCAT({0}, ',')";
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
}
