package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.security.UserPrincipal;
import top.blogapi.service.reaction.ReactionService;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/{blogId}")
    public ResponseEntity<ApiResponse> react(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Long blogId,
                                             @RequestParam String type) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.react(principal.getId(), blogId, type)));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ApiResponse> unreact(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long blogId) {
        reactionService.unreact(principal.getId(), blogId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<ApiResponse> getSummary(@PathVariable Long blogId,
                                                   @AuthenticationPrincipal UserPrincipal principal) {
        var summary = reactionService.getSummary(blogId);
        java.util.Map<String, Object> result = new java.util.HashMap<>(summary);
        if (principal != null) {
            result.put("userReaction", reactionService.getUserReaction(principal.getId(), blogId));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
