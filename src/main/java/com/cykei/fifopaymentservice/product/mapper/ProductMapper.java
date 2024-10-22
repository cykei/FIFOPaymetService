package com.cykei.fifopaymentservice.product.mapper;

import com.cykei.fifopaymentservice.product.Product;
import com.cykei.fifopaymentservice.product.ProductOption;
import com.cykei.fifopaymentservice.product.dto.OptionResponse;
import com.cykei.fifopaymentservice.product.dto.ProductDetailResponse;
import com.cykei.fifopaymentservice.product.repository.dto.ProductDto;
import com.cykei.fifopaymentservice.product.dto.ProductResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface ProductMapper {

    @Named("toResponse")
    ProductResponse toResponse(ProductDto product);

    @IterableMapping(qualifiedByName = "toResponse")
    List<ProductResponse> toResponses(List<ProductDto> products);

    @Mapping(source = "productOptions", target = "optionResponses")
    ProductDetailResponse toDetailResponse(Product product);

    @Named("toOptionResponse")
    OptionResponse toOptionResponse(ProductOption productOption);

    @IterableMapping(qualifiedByName = "toOptionResponse")
    List<OptionResponse> toOptionResponses(List<ProductOption> productOptions);

}