package top.blogapi.content.blog.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.content.blog.application.command.CreateBlogCommand;
import top.blogapi.content.blog.application.command.UpdateBlogCommand;
import top.blogapi.content.blog.application.command.PublishBlogCommand;
import top.blogapi.content.blog.application.command.DeleteBlogCommand;
import top.blogapi.content.blog.application.query.GetBlogQuery;
import top.blogapi.content.blog.interfaces.dto.BlogMapper;
import top.blogapi.content.blog.interfaces.dto.BlogResponse;
import top.blogapi.content.blog.interfaces.dto.CreateBlogRequest;
import top.blogapi.content.blog.interfaces.dto.UpdateBlogRequest;
import top.blogapi.infra.security.UserPrincipal;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.shared.response.PageResponse;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final CreateBlogCommand createBlogCommand;
    private final UpdateBlogCommand updateBlogCommand;
    private final PublishBlogCommand publishBlogCommand;
    private final DeleteBlogCommand deleteBlogCommand;
    private final GetBlogQuery getBlogQuery;
    private final BlogMapper blogMapper;

    @GetMapping
    public ResponseEntity<PageResponse<BlogResponse>> getPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = getBlogQuery.getPublished(page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = getBlogQuery.countPublished();
        return ResponseEntity.ok(PageResponse.of(blogs, page, size, total));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(getBlogQuery.execute(id))));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(getBlogQuery.getBySlug(slug))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody CreateBlogRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(createBlogCommand.execute(request, principal.getId()))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id,
                                              @Valid @RequestBody UpdateBlogRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(updateBlogCommand.execute(id, request, principal.getId()))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        deleteBlogCommand.execute(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse> publish(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(blogMapper.toResponse(publishBlogCommand.execute(id, principal.getId()))));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse> getTrending(@RequestParam(defaultValue = "10") int limit) {
        var blogs = getBlogQuery.getTrending(limit).stream()
                .map(blogMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(blogs));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<BlogResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = getBlogQuery.search(q, page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = getBlogQuery.countSearch(q);
        return ResponseEntity.ok(PageResponse.of(blogs, page, size, total));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<PageResponse<BlogResponse>> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var blogs = getBlogQuery.getByAuthor(authorId, page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        var total = getBlogQuery.countByAuthor(authorId);
        return ResponseEntity.ok(PageResponse.of(blogs, page, size, total));
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<ApiResponse> incrementView(@PathVariable Long id, HttpServletRequest request) {
        getBlogQuery.incrementView(id, request.getSession().getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
