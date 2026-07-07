package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.mapper.StoryMapper;
import top.blogapi.dto.request.story.StoryRequest;
import top.blogapi.dto.response.StoryResponse;
import top.blogapi.orchestrator.StoryOrchestrator;
import top.blogapi.security.UserPrincipal;

import java.util.List;

/**
 * Quản lý story tạm thời: tạo, danh sách đang hoạt động/feed, lưu trữ và xoá.
 */
@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryOrchestrator storyOrchestrator;
    private final StoryMapper storyMapper;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody StoryRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                storyMapper.toResponse(storyOrchestrator.createStory(request, principal.getId()))));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActive(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        List<StoryResponse> stories = storyOrchestrator.getActiveStories(principal.getId()).stream()
                .map(storyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(stories));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse> getFeed() {
        List<StoryResponse> stories = storyOrchestrator.getActiveFeed().stream()
                .map(storyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(stories));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getByUser(@PathVariable Long userId) {
        List<StoryResponse> stories = storyOrchestrator.getActiveStoriesByUser(userId).stream()
                .map(storyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(stories));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        storyOrchestrator.deleteStory(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/archive")
    public ResponseEntity<ApiResponse> getArchive(@AuthenticationPrincipal UserPrincipal principal,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        List<StoryResponse> archives = storyOrchestrator.getArchivedStories(principal.getId(), page, size).stream()
                .map(storyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(archives));
    }
}
