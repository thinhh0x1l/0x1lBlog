package top.blogapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.BookmarkOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý bookmark của người dùng: danh sách, bật/tắt, xoá và kiểm tra trạng thái.
 */
@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    @Data
    public static class BookmarkRequest {
        @NotNull
        private Long blogId;
        private String collection;
        private String note;
        private Boolean isPublic;
    }

    private final BookmarkOrchestrator bookmarkOrchestrator;

    @GetMapping
    public ResponseEntity<ApiResponse> getMyBookmarks(@AuthenticationPrincipal UserPrincipal principal,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(bookmarkOrchestrator.getByUserId(principal.getId(), page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> toggle(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody BookmarkRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var bookmark = new top.blogapi.model.entity.Bookmark();
        bookmark.setUserId(principal.getId());
        bookmark.setBlogId(request.getBlogId());
        bookmark.setCollection(request.getCollection());
        bookmark.setNote(request.getNote());
        bookmark.setIsPublic(request.getIsPublic());
        bookmarkOrchestrator.toggle(bookmark);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ApiResponse> remove(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long blogId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        bookmarkOrchestrator.remove(principal.getId(), blogId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/check/{blogId}")
    public ResponseEntity<ApiResponse> isBookmarked(@AuthenticationPrincipal UserPrincipal principal,
                                                    @PathVariable Long blogId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(bookmarkOrchestrator.isBookmarked(principal.getId(), blogId)));
    }
}
