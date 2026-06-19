package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.Follow;
import top.blogapi.security.UserPrincipal;
import top.blogapi.service.follow.FollowService;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> follow(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long userId) {
        followService.follow(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> unfollow(@AuthenticationPrincipal UserPrincipal principal,
                                                @PathVariable Long userId) {
        followService.unfollow(principal.getId(), userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<ApiResponse> isFollowing(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(followService.isFollowing(principal.getId(), userId)));
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<ApiResponse> getFollowers(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(followService.getFollowers(userId, page, size)));
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<ApiResponse> getFollowing(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(followService.getFollowing(userId, page, size)));
    }
}
