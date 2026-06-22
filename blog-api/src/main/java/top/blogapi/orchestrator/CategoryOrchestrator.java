package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.dto.mapper.CategoryMapper;
import top.blogapi.dto.response.CategoryResponse;
import top.blogapi.model.entity.Category;
import top.blogapi.service.category.CategoryService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryOrchestrator {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllVisible() {
        return categoryService.findAllVisible().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getById(Long id) {
        return categoryMapper.toResponse(categoryService.findById(id));
    }

    public CategoryResponse getBySlug(String slug) {
        return categoryMapper.toResponse(categoryService.findBySlug(slug));
    }

    public CategoryResponse create(Category category) {
        return categoryMapper.toResponse(categoryService.create(category));
    }

    public CategoryResponse update(Long id, Category category) {
        category.setId(id);
        return categoryMapper.toResponse(categoryService.update(category));
    }

    public void delete(Long id) {
        categoryService.softDelete(id);
    }

    public List<Category> getAll() {
        return categoryService.findAll();
    }
}
