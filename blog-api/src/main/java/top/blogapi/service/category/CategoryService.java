package top.blogapi.service.category;

import top.blogapi.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    List<Category> findAllVisible();
    Category findById(Long id);
    Category findBySlug(String slug);
    Category create(Category category);
    Category update(Category category);
    void softDelete(Long id);
    boolean existsBySlug(String slug);
}
