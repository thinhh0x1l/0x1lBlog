package top.blogapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

/**
 * Xử lý các thao tác CRUD blog, xuất bản, tìm kiếm, thịnh hành và theo dõi lượt xem.
 */
@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogOrchestrator blogOrchestrator;
    private final BlogMapper blogMapper;

    @GetMapping
    public ResponseEntity<PagedResponse<BlogResponse>> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = blogOrchestrator.getPublished(page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = blogOrchestrator.countPublished();
        return ResponseEntity.ok(PagedResponse.of(blogs, page, size, total));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(blogOrchestrator.getBlog(id))));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(blogOrchestrator.getBlogBySlug(slug))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody CreateBlogRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(blogOrchestrator.createBlog(request, principal.getId()))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateBlogRequest request) {
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(blogOrchestrator.updateBlog(id, request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        blogOrchestrator.deleteBlog(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(blogOrchestrator.publishBlog(id))));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse> getTrending(@RequestParam(defaultValue = "10") int limit) {
        var blogs = blogOrchestrator.getTrending(limit).stream()
                .map(blogMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(blogs));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<BlogResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = blogOrchestrator.search(q, page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = blogOrchestrator.countSearch(q);
        return ResponseEntity.ok(PagedResponse.of(blogs, page, size, total));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<PagedResponse<BlogResponse>> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = blogOrchestrator.getByAuthor(authorId, page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = blogOrchestrator.countByAuthor(authorId);
        return ResponseEntity.ok(PagedResponse.of(blogs, page, size, total));
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<ApiResponse> incrementView(@PathVariable Long id, HttpServletRequest request) {
        blogOrchestrator.incrementView(id, request.getSession().getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
