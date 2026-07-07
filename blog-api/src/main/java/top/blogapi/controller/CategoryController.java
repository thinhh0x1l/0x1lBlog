package top.blogapi.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.CategoryOrchestrator;

/**
 * Endpoint công khai để duyệt và quản lý danh mục blog.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    @Data
    public static class CategoryRequest {
        private String name;
        private String slug;
        private String description;
        private String icon;
        private String color;
        private Integer sortOrder;
    }

    private final CategoryOrchestrator categoryOrchestrator;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.getAllVisible()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.getById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.getBySlug(slug)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CategoryRequest request) {
        var category = new top.blogapi.model.entity.Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());
        category.setSortOrder(request.getSortOrder());
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.create(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        var category = new top.blogapi.model.entity.Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());
        category.setSortOrder(request.getSortOrder());
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.update(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        categoryOrchestrator.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
