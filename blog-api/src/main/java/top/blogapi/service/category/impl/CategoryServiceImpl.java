package top.blogapi.service.category.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Category;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.category.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "categories", key = "'findAll'")
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Cacheable(value = "categories", key = "'findAllVisible'")
    public List<Category> findAllVisible() {
        return categoryRepository.findAllVisible();
    }

    @Override
    @Cacheable(value = "categories", key = "'findById:' + #id")
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "categories", key = "'findBySlug:' + #slug")
    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category create(Category category) {
        categoryRepository.insert(category);
        return category;
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category update(Category category) {
        categoryRepository.update(category);
        return findById(category.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void softDelete(Long id) {
        categoryRepository.softDelete(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }
}
