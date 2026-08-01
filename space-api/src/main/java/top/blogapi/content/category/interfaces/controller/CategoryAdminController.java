package top.blogapi.content.category.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.content.category.application.command.CreateCategoryCommand;
import top.blogapi.content.category.application.command.UpdateCategoryCommand;
import top.blogapi.content.category.application.command.DeleteCategoryCommand;
import top.blogapi.content.category.application.query.GetCategoryQuery;
import top.blogapi.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final GetCategoryQuery getCategoryQuery;
    private final CreateCategoryCommand createCategoryCommand;
    private final UpdateCategoryCommand updateCategoryCommand;
    private final DeleteCategoryCommand deleteCategoryCommand;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(getCategoryQuery.getAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CategoryController.CreateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(createCategoryCommand.execute(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id,
                                              @Valid @RequestBody CategoryController.UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(updateCategoryCommand.execute(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        deleteCategoryCommand.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
