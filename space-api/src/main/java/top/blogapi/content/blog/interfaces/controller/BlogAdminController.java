package top.blogapi.content.blog.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.content.blog.application.command.ToggleTopCommand;
import top.blogapi.content.blog.application.command.ToggleRecommendCommand;
import top.blogapi.content.blog.application.command.DeleteAdminBlogCommand;
import top.blogapi.content.blog.application.query.GetAdminBlogQuery;
import top.blogapi.content.blog.interfaces.dto.BlogMapper;
import top.blogapi.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class BlogAdminController {

    private final GetAdminBlogQuery getAdminBlogQuery;
    private final ToggleTopCommand toggleTopCommand;
    private final ToggleRecommendCommand toggleRecommendCommand;
    private final DeleteAdminBlogCommand deleteAdminBlogCommand;
    private final BlogMapper blogMapper;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        var blogs = getAdminBlogQuery.execute(page, size).stream()
                .map(blogMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(blogs));
    }

    @PutMapping("/{id}/top")
    public ResponseEntity<ApiResponse> toggleTop(@PathVariable Long id, @RequestParam boolean isTop) {
        toggleTopCommand.execute(id, isTop);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}/recommend")
    public ResponseEntity<ApiResponse> toggleRecommend(@PathVariable Long id, @RequestParam boolean isRecommend) {
        toggleRecommendCommand.execute(id, isRecommend);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        deleteAdminBlogCommand.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
