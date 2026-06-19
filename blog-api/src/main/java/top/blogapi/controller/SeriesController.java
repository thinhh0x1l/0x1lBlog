package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.BlogSeries;
import top.blogapi.service.series.BlogSeriesService;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final BlogSeriesService blogSeriesService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(blogSeriesService.findById(id)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(blogSeriesService.getByAuthorId(authorId, page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody BlogSeries series) {
        return ResponseEntity.ok(ApiResponse.success(blogSeriesService.create(series)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody BlogSeries series) {
        series.setId(id);
        return ResponseEntity.ok(ApiResponse.success(blogSeriesService.update(series)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        blogSeriesService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{seriesId}/blogs/{blogId}")
    public ResponseEntity<ApiResponse> addBlog(@PathVariable Long seriesId,
                                               @PathVariable Long blogId,
                                               @RequestParam(defaultValue = "0") int sortOrder) {
        blogSeriesService.addBlog(seriesId, blogId, sortOrder);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{seriesId}/blogs/{blogId}")
    public ResponseEntity<ApiResponse> removeBlog(@PathVariable Long seriesId, @PathVariable Long blogId) {
        blogSeriesService.removeBlog(seriesId, blogId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
