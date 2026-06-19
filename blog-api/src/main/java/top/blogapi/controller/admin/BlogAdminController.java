package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.Blog;
import top.blogapi.repository.BlogRepository;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class BlogAdminController {

    private final BlogRepository blogRepository;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        List<Blog> blogs = blogRepository.findPublished(size, page * size);
        return ResponseEntity.ok(ApiResponse.success(blogs));
    }

    @PutMapping("/{id}/top")
    public ResponseEntity<ApiResponse> toggleTop(@PathVariable Long id, @RequestParam boolean isTop) {
        blogRepository.toggleTop(id, isTop);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}/recommend")
    public ResponseEntity<ApiResponse> toggleRecommend(@PathVariable Long id, @RequestParam boolean isRecommend) {
        blogRepository.toggleRecommend(id, isRecommend);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        blogRepository.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
