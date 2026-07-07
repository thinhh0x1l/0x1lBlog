package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.skill.UnlockRequest;
import top.blogapi.orchestrator.SkillTreeOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý duyệt cây kỹ năng, theo dõi tiến độ, mở khoá kỹ năng và đặt lại.
 */
@RestController
@RequestMapping("/api/skill-trees")
@RequiredArgsConstructor
public class SkillTreeController {

    private final SkillTreeOrchestrator skillTreeOrchestrator;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(skillTreeOrchestrator.getSkillTrees(principal.getId())));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getByCategory(@PathVariable Long categoryId,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                skillTreeOrchestrator.getSkillTreesByCategory(categoryId, principal.getId())));
    }

    @GetMapping("/my-progress")
    public ResponseEntity<ApiResponse> getMyProgress(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(skillTreeOrchestrator.getUserProgress(principal.getId())));
    }

    @PostMapping("/unlock")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unlock(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody UnlockRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        skillTreeOrchestrator.unlockSkill(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> reset(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        skillTreeOrchestrator.resetSkillTree(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(skillTreeOrchestrator.getUserProgress(userId)));
    }
}
