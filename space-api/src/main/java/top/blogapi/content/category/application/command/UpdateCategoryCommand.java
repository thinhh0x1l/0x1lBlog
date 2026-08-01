package top.blogapi.content.category.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.content.category.domain.service.CategoryService;
import top.blogapi.content.category.interfaces.controller.CategoryController.UpdateCategoryRequest;
import top.blogapi.content.category.interfaces.dto.CategoryMapper;
import top.blogapi.content.category.interfaces.dto.CategoryResponse;

@Service
@RequiredArgsConstructor
public class UpdateCategoryCommand {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse execute(Long id, UpdateCategoryRequest request) {
        Category category = categoryService.findById(id);
        if (request.name() != null) category.setName(request.name());
        if (request.slug() != null) category.setSlug(request.slug());
        if (request.description() != null) category.setDescription(request.description());
        if (request.icon() != null) category.setIcon(request.icon());
        if (request.color() != null) category.setColor(request.color());
        if (request.sortOrder() != null) category.setSortOrder(request.sortOrder());
        return categoryMapper.toResponse(categoryService.update(category));
    }
}
