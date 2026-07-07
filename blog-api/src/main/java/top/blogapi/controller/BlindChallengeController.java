package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.blind.BlindGuessRequest;
import top.blogapi.orchestrator.BlindChallengeOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý thử thách đoán mù hàng ngày, lượt đoán, bảng xếp hạng và tiết lộ chủ đề.
 */
@RestController
@RequestMapping("/api/blind")
@RequiredArgsConstructor
public class BlindChallengeController {

    private final BlindChallengeOrchestrator blindChallengeOrchestrator;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse> getTodayChallenge() {
        return ResponseEntity.ok(ApiResponse.success(blindChallengeOrchestrator.getTodayChallenge()));
    }

    @PostMapping("/guess")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> makeGuess(@AuthenticationPrincipal UserPrincipal principal,
                                                  @Valid @RequestBody BlindGuessRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var guess = blindChallengeOrchestrator.makeGuess(principal.getId(), request.getGuessedTopicId());
        return ResponseEntity.ok(ApiResponse.success(guess));
    }

    @GetMapping("/my-guess")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getMyGuess(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var guess = blindChallengeOrchestrator.getCurrentGuess(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(guess));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse> getLeaderboard() {
        var today = java.time.LocalDate.now();
        return ResponseEntity.ok(ApiResponse.success(blindChallengeOrchestrator.getLeaderboard(today)));
    }

    @PostMapping("/reveal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> revealTopic() {
        var challenge = blindChallengeOrchestrator.revealTopic();
        return ResponseEntity.ok(ApiResponse.success(challenge));
    }
}
