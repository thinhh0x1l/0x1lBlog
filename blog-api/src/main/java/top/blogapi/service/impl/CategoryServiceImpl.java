package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.entity.Category;
import top.blogapi.mapper.CategoryMapper;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    @Override
    public List<CategoryResponse> getCategoryList() {
        return categoryRepository.getCategoryList().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @Transactional
    @Override
    public int saveCategory(Category category) {
        return categoryRepository.saveCategory(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.getCategoryById(id);
    }
}
