package com.cykei.fifopaymentservice.admin.service;

import com.cykei.fifopaymentservice.admin.Category;
import com.cykei.fifopaymentservice.admin.dto.CategoryRequest;
import com.cykei.fifopaymentservice.admin.dto.CategoryResponse;
import com.cykei.fifopaymentservice.admin.mapper.CategoryMapper;
import com.cykei.fifopaymentservice.admin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public void createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        if (categoryRepository.findByCategoryName(category.getCategoryName()).isPresent()) {
            throw new IllegalArgumentException("이미 있는 카테고리 이름 입니다.");
        }
        categoryRepository.save(category);
    }


    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 이름입니다."));
        return categoryMapper.toResponse(category);
    }


    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponses(categories);
    }


    @Transactional
    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 이름입니다."));
        try {
            category.updateCategoryName(categoryRequest.getCategoryName());

        } catch (Exception e) {
            throw new IllegalArgumentException("존재하는 카테고리 이름으로는 변경할 수 없습니다.");
        }
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new RuntimeException("Category not found");
        }
    }
}
