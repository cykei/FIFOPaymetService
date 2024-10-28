package com.fifo.wishservice.mapper;

import com.fifo.wishservice.client.dto.ProductResponse;
import com.fifo.wishservice.service.dto.WishCreateRequest;
import com.fifo.wishservice.service.dto.WishProductResponse;
import com.fifo.wishservice.entity.Wish;
import com.fifo.wishservice.service.dto.WishResponse;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface WishMapper {

    @Mapping(source = "wish.productId", target = "productId")
    WishProductResponse toWishProductResponse(Wish wish, ProductResponse product);

    default List<WishProductResponse> toWishProductResponses(List<Wish> wishes, List<ProductResponse> products) {
        Map<Long, ProductResponse> productMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::getProductId, product -> product));

        return wishes.stream()
                .map(wish -> {
                    ProductResponse product = productMap.get(wish.getProductId());
                    return toWishProductResponse(wish, product);
                })
                .collect(Collectors.toList());
    }

    Wish toEntity(WishCreateRequest request);

    @Named("toResponse")
    WishResponse toResponse(Wish wish);

    @IterableMapping(qualifiedByName = "toResponse")
    List<WishResponse> toResponses(List<Wish> wishes);
}
