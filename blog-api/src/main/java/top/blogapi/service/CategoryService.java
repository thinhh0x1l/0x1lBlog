package top.blogapi.service;

import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryResponse> getCategoryList();

    int saveCategory(Category category);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    int deleteCategoryById(Long id);

    int updateCategory(Category category);
}
