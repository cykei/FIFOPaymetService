package com.fifo.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank
    private String categoryName;
    private Long parentCategoryId;
}
