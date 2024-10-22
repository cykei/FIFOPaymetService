package com.cykei.fifopaymentservice.admin.mapper;

import com.cykei.fifopaymentservice.admin.Category;
import com.cykei.fifopaymentservice.admin.dto.CategoryRequest;
import com.cykei.fifopaymentservice.admin.dto.CategoryResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);

    @Named("toResponse")
    CategoryResponse toResponse(Category category);

    @IterableMapping(qualifiedByName = "toResponse")
    List<CategoryResponse> toResponses(List<Category> categories);
}
