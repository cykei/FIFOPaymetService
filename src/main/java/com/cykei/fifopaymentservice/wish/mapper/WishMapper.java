package com.cykei.fifopaymentservice.wish.mapper;

import com.cykei.fifopaymentservice.product.Product;
import com.cykei.fifopaymentservice.wish.Wish;
import com.cykei.fifopaymentservice.wish.dto.WishCreateRequest;
import com.cykei.fifopaymentservice.wish.dto.WishProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface WishMapper {

    @Mapping(source = "wish.productId", target = "productId")
    WishProductResponse toResponse(Wish wish, Product product);

    default List<WishProductResponse> toResponses(List<Wish> wishes, List<Product> products) {
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        return wishes.stream()
                .map(wish -> {
                    Product product = productMap.get(wish.getProductId());
                    return toResponse(wish, product);
                })
                .collect(Collectors.toList());
    }

    Wish toEntity(WishCreateRequest request);
}
