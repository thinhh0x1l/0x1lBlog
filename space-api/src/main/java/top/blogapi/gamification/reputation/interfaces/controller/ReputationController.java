package top.blogapi.gamification.reputation.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.gamification.reputation.application.query.GetReputationQuery;

@RestController
@RequestMapping("/api/reputation")
@RequiredArgsConstructor
public class ReputationController {

    private final GetReputationQuery getReputationQuery;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getScores(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(getReputationQuery.execute(userId)));
    }
}
