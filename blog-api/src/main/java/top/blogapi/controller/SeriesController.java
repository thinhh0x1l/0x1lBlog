package top.blogapi.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.SeriesOrchestrator;

/**
 * Quản lý chuỗi blog: CRUD, danh sách theo tác giả và liên kết blog với chuỗi.
 */
@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    @Data
    public static class SeriesRequest {
        private String name;
        private String description;
        private String coverImage;
    }

    private final SeriesOrchestrator seriesOrchestrator;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(seriesOrchestrator.getById(id)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(seriesOrchestrator.getByAuthor(authorId, page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody SeriesRequest request) {
        var series = new top.blogapi.model.entity.BlogSeries();
        series.setName(request.getName());
        series.setDescription(request.getDescription());
        series.setCoverImage(request.getCoverImage());
        return ResponseEntity.ok(ApiResponse.success(seriesOrchestrator.create(series)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody SeriesRequest request) {
        var series = new top.blogapi.model.entity.BlogSeries();
        series.setName(request.getName());
        series.setDescription(request.getDescription());
        series.setCoverImage(request.getCoverImage());
        return ResponseEntity.ok(ApiResponse.success(seriesOrchestrator.update(id, series)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        seriesOrchestrator.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{seriesId}/blogs/{blogId}")
    public ResponseEntity<ApiResponse> addBlog(@PathVariable Long seriesId,
                                               @PathVariable Long blogId,
                                               @RequestParam(defaultValue = "0") int sortOrder) {
        seriesOrchestrator.addBlog(seriesId, blogId, sortOrder);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{seriesId}/blogs/{blogId}")
    public ResponseEntity<ApiResponse> removeBlog(@PathVariable Long seriesId, @PathVariable Long blogId) {
        seriesOrchestrator.removeBlog(seriesId, blogId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
