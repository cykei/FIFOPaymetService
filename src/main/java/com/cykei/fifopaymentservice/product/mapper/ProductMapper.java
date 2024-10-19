package com.cykei.fifopaymentservice.product.mapper;

import com.cykei.fifopaymentservice.product.dto.ProductDto;
import com.cykei.fifopaymentservice.product.dto.ProductResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Named("mapToResponse")
    ProductResponse mapToResponse(ProductDto product);

    @IterableMapping(qualifiedByName = "mapToResponse")
    List<ProductResponse> mapToResponses(List<ProductDto> products);
}