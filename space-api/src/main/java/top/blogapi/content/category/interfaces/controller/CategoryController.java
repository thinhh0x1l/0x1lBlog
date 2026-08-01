package top.blogapi.content.category.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.content.category.application.command.CreateCategoryCommand;
import top.blogapi.content.category.application.command.UpdateCategoryCommand;
import top.blogapi.content.category.application.command.DeleteCategoryCommand;
import top.blogapi.content.category.application.query.GetCategoryQuery;
import top.blogapi.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    public record CreateCategoryRequest(
            @NotBlank(message = "Name is required") @Size(max = 100) String name,
            @NotBlank(message = "Slug is required") @Size(max = 100) String slug,
            @Size(max = 500) String description,
            @Size(max = 100) String icon,
            @Size(max = 20) String color,
            Integer sortOrder
    ) {}

    public record UpdateCategoryRequest(
            @Size(max = 100) String name,
            @Size(max = 100) String slug,
            @Size(max = 500) String description,
            @Size(max = 100) String icon,
            @Size(max = 20) String color,
            Integer sortOrder
    ) {}

    private final GetCategoryQuery getCategoryQuery;
    private final CreateCategoryCommand createCategoryCommand;
    private final UpdateCategoryCommand updateCategoryCommand;
    private final DeleteCategoryCommand deleteCategoryCommand;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(getCategoryQuery.getAllVisible()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(getCategoryQuery.getById(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(getCategoryQuery.getBySlug(slug)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(createCategoryCommand.execute(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(updateCategoryCommand.execute(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        deleteCategoryCommand.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
