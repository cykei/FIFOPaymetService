package com.fifo.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private Long parentCategoryId;
}
