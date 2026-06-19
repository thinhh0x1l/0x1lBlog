package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.mapper.CategoryMapper;
import top.blogapi.dto.response.CategoryResponse;
import top.blogapi.model.entity.Category;
import top.blogapi.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        List<CategoryResponse> categories = categoryService.findAllVisible().stream()
                .map(categoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(categoryService.findById(id))));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(categoryService.findBySlug(slug))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody Category category) {
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(categoryService.create(category))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toResponse(categoryService.update(category))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        categoryService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
