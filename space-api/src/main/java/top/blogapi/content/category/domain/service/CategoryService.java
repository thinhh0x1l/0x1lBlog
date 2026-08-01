package top.blogapi.content.category.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.content.category.domain.repository.CategoryRepository;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;
import top.blogapi.infra.cache.CachePolicies;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllVisible() {
        return categoryRepository.findAllVisible();
    }

    public Category findById(Long id) {
        return cacheService.get(
                CacheKey.category(id),
                Category.class,
                () -> categoryRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)),
                CachePolicies.CATEGORY
        );
    }

    public Category findBySlug(String slug) {
        return cacheService.get(
                CacheKey.categoryBySlug(slug),
                Category.class,
                () -> categoryRepository.findBySlug(slug)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)),
                CachePolicies.CATEGORY
        );
    }

    public Category create(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public Category update(Category category) {
        categoryRepository.save(category);
        Category updated = findById(category.getId());
        cacheService.evict(CacheKey.category(updated.getId()));
        if (updated.getSlug() != null) {
            cacheService.evict(CacheKey.categoryBySlug(updated.getSlug()));
        }
        return updated;
    }

    public void softDelete(Long id) {
        categoryRepository.softDelete(id);
        cacheService.evict(CacheKey.category(id));
    }

    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }
}
