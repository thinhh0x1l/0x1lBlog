package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.ReputationOrchestrator;

/**
 * Endpoint để lấy điểm uy tín của người dùng.
 */
@RestController
@RequestMapping("/api/reputation")
@RequiredArgsConstructor
public class ReputationController {

    private final ReputationOrchestrator reputationOrchestrator;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getScores(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(reputationOrchestrator.getScores(userId)));
    }
}
