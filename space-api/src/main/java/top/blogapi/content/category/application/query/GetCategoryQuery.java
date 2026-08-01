package top.blogapi.content.category.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.content.category.domain.service.CategoryService;
import top.blogapi.content.category.interfaces.dto.CategoryMapper;
import top.blogapi.content.category.interfaces.dto.CategoryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoryQuery {

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

    public List<Category> getAll() {
        return categoryService.findAll();
    }
}
