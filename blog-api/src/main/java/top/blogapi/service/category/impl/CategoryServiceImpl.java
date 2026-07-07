package top.blogapi.service.category.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Category;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.cache.CachePolicies;
import top.blogapi.service.category.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai CategoryService với cache, cung cấp CRUD,
 * tra cứu theo slug và xóa cache khi cập nhật.
 */
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllVisible() {
        return categoryRepository.findAllVisible();
    }

    @Override
    public Category findById(Long id) {
        return cacheService.get(
                CacheKey.category(id),
                Category.class,
                () -> categoryRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)),
                CachePolicies.CATEGORY
        );
    }

    @Override
    public Category findBySlug(String slug) {
        return cacheService.get(
                CacheKey.categoryBySlug(slug),
                Category.class,
                () -> categoryRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)),
                CachePolicies.CATEGORY
        );
    }

    @Override
    public Category create(Category category) {
        categoryRepository.insert(category);
        return category;
    }

    @Override
    public Category update(Category category) {
        categoryRepository.update(category);
        Category updated = findById(category.getId());
        cacheService.evict(CacheKey.category(updated.getId()));
        if (updated.getSlug() != null) {
            cacheService.evict(CacheKey.categoryBySlug(updated.getSlug()));
        }
        return updated;
    }

    @Override
    public void softDelete(Long id) {
        categoryRepository.softDelete(id);
        cacheService.evict(CacheKey.category(id));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }
}
