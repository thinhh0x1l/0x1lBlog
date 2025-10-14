package top.blogapi.service;

import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getCategoryList();

    int saveCategory(Category category);

    Category getCategoryById(Long id);
}
