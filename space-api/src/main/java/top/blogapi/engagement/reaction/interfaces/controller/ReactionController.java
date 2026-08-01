package top.blogapi.engagement.reaction.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.engagement.reaction.application.command.ReactCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactCommand reactCommand;

    @PostMapping
    public ResponseEntity<ApiResponse> react(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestParam String targetType,
                                              @RequestParam Long targetId,
                                              @RequestParam String type) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        reactCommand.react(targetType, targetId, principal.getId(), type);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> unreact(@AuthenticationPrincipal UserPrincipal principal,
                                                @RequestParam String targetType,
                                                @RequestParam Long targetId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        reactCommand.unreact(targetType, targetId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSummary(@RequestParam String targetType,
                                                   @RequestParam Long targetId,
                                                   @AuthenticationPrincipal UserPrincipal principal) {
        var result = reactCommand.getSummary(targetType, targetId, principal != null ? principal.getId() : null);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
