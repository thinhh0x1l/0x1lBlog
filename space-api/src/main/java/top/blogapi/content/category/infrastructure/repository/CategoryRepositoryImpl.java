package top.blogapi.content.category.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.content.category.domain.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpa;
    private final CategoryMybatisMapper mybatis;

    @Override
    public Optional<Category> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return jpa.findBySlug(slug);
    }

    @Override
    public List<Category> findAll() {
        return jpa.findAllByOrderBySortOrder();
    }

    @Override
    public List<Category> findAllVisible() {
        return jpa.findAllByIsVisibleTrueOrderBySortOrder();
    }

    @Override
    public void save(Category category) {
        jpa.save(category);
    }

    @Override
    public void softDelete(Long id) {
        jpa.softDelete(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpa.existsBySlug(slug);
    }

    @Override
    public void refreshBlogCount(Long categoryId) {
        mybatis.refreshBlogCount(categoryId);
    }
}
