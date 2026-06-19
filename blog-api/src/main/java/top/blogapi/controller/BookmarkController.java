package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.Bookmark;
import top.blogapi.security.UserPrincipal;
import top.blogapi.service.bookmark.BookmarkService;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<ApiResponse> getMyBookmarks(@AuthenticationPrincipal UserPrincipal principal,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(bookmarkService.getByUserId(principal.getId(), page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> toggle(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody Bookmark bookmark) {
        bookmark.setUserId(principal.getId());
        bookmarkService.toggle(bookmark);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ApiResponse> remove(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long blogId) {
        bookmarkService.remove(principal.getId(), blogId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/check/{blogId}")
    public ResponseEntity<ApiResponse> isBookmarked(@AuthenticationPrincipal UserPrincipal principal,
                                                    @PathVariable Long blogId) {
        return ResponseEntity.ok(ApiResponse.success(bookmarkService.isBookmarked(principal.getId(), blogId)));
    }
}
