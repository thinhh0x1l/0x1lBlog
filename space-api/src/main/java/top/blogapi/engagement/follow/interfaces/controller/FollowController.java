package top.blogapi.engagement.follow.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.engagement.follow.application.command.ToggleFollowCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final ToggleFollowCommand toggleFollowCommand;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> follow(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long userId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        toggleFollowCommand.follow(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> unfollow(@AuthenticationPrincipal UserPrincipal principal,
                                                @PathVariable Long userId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        toggleFollowCommand.unfollow(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<ApiResponse> isFollowing(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long userId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(toggleFollowCommand.isFollowing(principal.getId(), userId)));
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<ApiResponse> getFollowers(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(toggleFollowCommand.getFollowers(userId, page, size)));
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<ApiResponse> getFollowing(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(toggleFollowCommand.getFollowing(userId, page, size)));
    }
}
