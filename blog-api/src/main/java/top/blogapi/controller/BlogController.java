package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.common.response.PagedResponse;
import top.blogapi.dto.mapper.BlogMapper;
import top.blogapi.dto.request.blog.CreateBlogRequest;
import top.blogapi.dto.request.blog.UpdateBlogRequest;
import top.blogapi.dto.response.BlogResponse;
import top.blogapi.orchestrator.BlogOrchestrator;
import top.blogapi.security.UserPrincipal;
import top.blogapi.service.blog.BlogService;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final BlogOrchestrator blogOrchestrator;
    private final BlogMapper blogMapper;

    @GetMapping
    public ResponseEntity<PagedResponse<BlogResponse>> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = blogService.getPublished(page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = blogService.countPublished();
        return ResponseEntity.ok(PagedResponse.of(blogs, page, size, total));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(blogOrchestrator.getBlog(id)));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(blogOrchestrator.getBlogBySlug(slug)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody CreateBlogRequest request) {
        return ResponseEntity.ok(ApiResponse.success(blogOrchestrator.createBlog(request, principal.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody UpdateBlogRequest request) {
        return ResponseEntity.ok(ApiResponse.success(blogOrchestrator.updateBlog(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        blogOrchestrator.deleteBlog(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(blogOrchestrator.publishBlog(id)));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse> getTrending(@RequestParam(defaultValue = "10") int limit) {
        var blogs = blogService.getTrending(limit).stream()
                .map(blogMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(blogs));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<BlogResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = blogService.search(q, page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = blogs.size();
        return ResponseEntity.ok(PagedResponse.of(blogs, page, size, total));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<PagedResponse<BlogResponse>> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = blogService.getByAuthorId(authorId, page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = blogService.countByAuthorId(authorId);
        return ResponseEntity.ok(PagedResponse.of(blogs, page, size, total));
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<ApiResponse> incrementView(@PathVariable Long id) {
        blogService.incrementViews(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
