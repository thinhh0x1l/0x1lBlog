package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.StreakOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý theo dõi và truy xuất chuỗi điểm danh hàng ngày.
 */
@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class StreakController {

    private final StreakOrchestrator streakOrchestrator;

    @PostMapping("/checkin")
    public ResponseEntity<ApiResponse> checkin(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        int streak = streakOrchestrator.checkin(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Điểm danh thành công", streak));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getStreak(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        int streak = streakOrchestrator.getStreak(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(streak));
    }
}
