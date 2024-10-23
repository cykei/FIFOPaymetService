package com.cykei.fifopaymentservice.admin.controller;

import com.cykei.fifopaymentservice.admin.dto.CategoryRequest;
import com.cykei.fifopaymentservice.admin.dto.CategoryResponse;
import com.cykei.fifopaymentservice.admin.service.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public void createCategory(@RequestBody CategoryRequest categoryRequest) {
        adminCategoryService.createCategory(categoryRequest);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return adminCategoryService.getCategoryById(id);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return adminCategoryService.getAllCategories();
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        adminCategoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
    }

}
