package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.request.quest.ClaimRewardRequest;
import top.blogapi.orchestrator.QuestOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Xử lý duyệt danh mục nhiệm vụ, giao nhiệm vụ, theo dõi tiến độ và nhận thưởng.
 */
@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestOrchestrator questOrchestrator;

    @GetMapping
    public ResponseEntity<ApiResponse> getQuestCatalog(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(questOrchestrator.getActiveQuests()));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getMyQuests(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(questOrchestrator.getUserQuests(principal.getId())));
    }

    @PostMapping("/assign/{questId}")
    public ResponseEntity<ApiResponse> assignQuest(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long questId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                questOrchestrator.assignQuest(principal.getId(), questId)));
    }

    @PostMapping("/{userQuestId}/claim")
    public ResponseEntity<ApiResponse> claimReward(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long userQuestId,
                                                   @RequestBody(required = false) ClaimRewardRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        Long id = request != null ? request.getUserQuestId() : userQuestId;
        return ResponseEntity.ok(ApiResponse.success(
                questOrchestrator.claimReward(principal.getId(), id)));
    }

    @PostMapping("/auto-assign")
    public ResponseEntity<ApiResponse> autoAssign(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                questOrchestrator.autoAssignQuests(principal.getId())));
    }

    @PostMapping("/progress")
    public ResponseEntity<ApiResponse> updateProgress(@AuthenticationPrincipal UserPrincipal principal,
                                                      @RequestParam String action) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                questOrchestrator.updateProgress(principal.getId(), action)));
    }
}
