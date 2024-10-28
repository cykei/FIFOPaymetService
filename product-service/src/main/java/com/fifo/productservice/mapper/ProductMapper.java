package com.fifo.productservice.mapper;

import com.fifo.productservice.entity.Product;
import com.fifo.productservice.entity.ProductOption;
import com.fifo.productservice.service.dto.OptionResponse;
import com.fifo.productservice.service.dto.ProductDetailResponse;
import com.fifo.productservice.service.dto.ProductResponse;
import com.fifo.productservice.repository.dto.ProductDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

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