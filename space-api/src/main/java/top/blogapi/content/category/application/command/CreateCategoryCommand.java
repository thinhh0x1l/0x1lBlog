package top.blogapi.content.category.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.content.category.domain.service.CategoryService;
import top.blogapi.content.category.interfaces.controller.CategoryController.CreateCategoryRequest;
import top.blogapi.content.category.interfaces.dto.CategoryMapper;
import top.blogapi.content.category.interfaces.dto.CategoryResponse;

@Service
@RequiredArgsConstructor
public class CreateCategoryCommand {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse execute(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setDescription(request.description());
        category.setIcon(request.icon());
        category.setColor(request.color());
        category.setSortOrder(request.sortOrder());
        return categoryMapper.toResponse(categoryService.create(category));
    }
}
