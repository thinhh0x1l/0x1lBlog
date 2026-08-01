package top.blogapi.gamification.streak.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.gamification.streak.application.command.CheckinCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class StreakController {

    private final CheckinCommand checkinCommand;

    @PostMapping("/checkin")
    public ResponseEntity<ApiResponse> checkin(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        int streak = checkinCommand.checkin(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Điểm danh thành công", streak));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getStreak(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        int streak = checkinCommand.getStreak(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(streak));
    }
}
