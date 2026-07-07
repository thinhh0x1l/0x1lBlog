package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.Category;
import top.blogapi.orchestrator.CategoryOrchestrator;

/**
 * Endpoint CRUD quản trị cho quản lý danh mục blog.
 */
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryOrchestrator categoryOrchestrator;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.getAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody Category category) {
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.create(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.ok(ApiResponse.success(categoryOrchestrator.update(id, category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        categoryOrchestrator.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
