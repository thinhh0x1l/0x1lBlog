package top.blogapi.gamification.skill.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.gamification.skill.interfaces.dto.UnlockRequest;
import top.blogapi.gamification.skill.application.query.GetSkillTreeQuery;
import top.blogapi.gamification.skill.application.command.UnlockSkillCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/skill-trees")
@RequiredArgsConstructor
public class SkillTreeController {

    private final GetSkillTreeQuery getSkillTreeQuery;
    private final UnlockSkillCommand unlockSkillCommand;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getSkillTreeQuery.getSkillTrees(principal.getId())));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getByCategory(@PathVariable Long categoryId,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                getSkillTreeQuery.getSkillTreesByCategory(categoryId, principal.getId())));
    }

    @GetMapping("/my-progress")
    public ResponseEntity<ApiResponse> getMyProgress(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getSkillTreeQuery.getUserProgress(principal.getId())));
    }

    @PostMapping("/unlock")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unlock(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody UnlockRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        unlockSkillCommand.unlockSkill(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> reset(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        unlockSkillCommand.resetSkillTree(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(getSkillTreeQuery.getUserProgress(userId)));
    }
}
