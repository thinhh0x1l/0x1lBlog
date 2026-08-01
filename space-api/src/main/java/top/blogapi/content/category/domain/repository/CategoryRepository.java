package top.blogapi.content.category.domain.repository;

import top.blogapi.content.category.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(Long id);
    Optional<Category> findBySlug(String slug);
    List<Category> findAll();
    List<Category> findAllVisible();
    void save(Category category);
    void softDelete(Long id);
    boolean existsBySlug(String slug);
    void refreshBlogCount(Long categoryId);
}
