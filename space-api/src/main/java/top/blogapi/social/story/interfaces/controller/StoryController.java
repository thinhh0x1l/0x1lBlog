package top.blogapi.social.story.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.social.story.interfaces.dto.StoryMapper;
import top.blogapi.social.story.interfaces.dto.StoryRequest;
import top.blogapi.social.story.interfaces.dto.StoryResponse;
import top.blogapi.social.story.application.command.CreateStoryCommand;
import top.blogapi.social.story.application.query.GetStoryQuery;
import top.blogapi.infra.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final CreateStoryCommand createStoryCommand;
    private final GetStoryQuery getStoryQuery;
    private final StoryMapper storyMapper;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody StoryRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                storyMapper.toResponse(createStoryCommand.execute(request, principal.getId()))));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse> getActive(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        List<StoryResponse> stories = getStoryQuery.getActiveStories(principal.getId()).stream()
                .map(storyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(stories));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse> getFeed() {
        List<StoryResponse> stories = getStoryQuery.getActiveFeed().stream()
                .map(storyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(stories));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getByUser(@PathVariable Long userId) {
        List<StoryResponse> stories = getStoryQuery.getActiveStoriesByUser(userId).stream()
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
        createStoryCommand.deleteStory(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/archive")
    public ResponseEntity<ApiResponse> getArchive(@AuthenticationPrincipal UserPrincipal principal,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        List<StoryResponse> archives = getStoryQuery.getArchivedStories(principal.getId(), page, size).stream()
                .map(storyMapper::toArchiveResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(archives));
    }
}
