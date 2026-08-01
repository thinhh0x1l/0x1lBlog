package top.blogapi.gamification.quest.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.gamification.quest.interfaces.dto.ClaimRewardRequest;
import top.blogapi.gamification.quest.application.query.GetQuestQuery;
import top.blogapi.gamification.quest.application.command.AssignQuestCommand;
import top.blogapi.gamification.quest.application.command.CompleteQuestCommand;
import top.blogapi.gamification.quest.application.command.ClaimQuestRewardCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final GetQuestQuery getQuestQuery;
    private final AssignQuestCommand assignQuestCommand;
    private final CompleteQuestCommand completeQuestCommand;
    private final ClaimQuestRewardCommand claimQuestRewardCommand;

    @GetMapping
    public ResponseEntity<ApiResponse> getQuestCatalog() {
        return ResponseEntity.ok(ApiResponse.success(getQuestQuery.getActiveQuests()));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getMyQuests(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getQuestQuery.getUserQuests(principal.getId())));
    }

    @PostMapping("/assign/{questId}")
    public ResponseEntity<ApiResponse> assignQuest(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long questId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                assignQuestCommand.execute(principal.getId(), questId)));
    }

    @PostMapping("/{userQuestId}/claim")
    public ResponseEntity<ApiResponse> claimReward(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long userQuestId,
                                                   @RequestBody(required = false) ClaimRewardRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        Long id = request != null ? request.userQuestId() : userQuestId;
        return ResponseEntity.ok(ApiResponse.success(
                claimQuestRewardCommand.execute(principal.getId(), id)));
    }

    @PostMapping("/auto-assign")
    public ResponseEntity<ApiResponse> autoAssign(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                getQuestQuery.autoAssignQuests(principal.getId())));
    }

    @PostMapping("/progress")
    public ResponseEntity<ApiResponse> updateProgress(@AuthenticationPrincipal UserPrincipal principal,
                                                      @RequestParam String action) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                completeQuestCommand.execute(principal.getId(), action)));
    }
}
