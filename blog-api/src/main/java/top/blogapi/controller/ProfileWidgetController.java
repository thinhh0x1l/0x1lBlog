package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.profile.WidgetDTO;
import top.blogapi.dto.profile.WidgetReorderRequest;
import top.blogapi.dto.profile.WidgetUpdateRequest;
import top.blogapi.orchestrator.ProfileWidgetOrchestrator;
import top.blogapi.security.UserPrincipal;

import java.util.List;

/**
 * Quản lý tuỳ chỉnh widget hồ sơ: hiển thị, nội dung, thứ tự và chế độ trò chơi.
 */
@RestController
@RequestMapping("/api/profile/widgets")
@RequiredArgsConstructor
public class ProfileWidgetController {

    private final ProfileWidgetOrchestrator profileWidgetOrchestrator;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<WidgetDTO>>> getWidgets(
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                profileWidgetOrchestrator.getWidgets(principal.getId())));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<WidgetDTO>> updateWidget(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WidgetUpdateRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                profileWidgetOrchestrator.updateWidget(principal.getId(), request)));
    }

    @PatchMapping("/{widgetType}/toggle")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<WidgetDTO>> toggleWidget(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String widgetType) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                profileWidgetOrchestrator.toggleWidget(principal.getId(), widgetType)));
    }

    @PutMapping("/reorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<WidgetDTO>>> reorderWidgets(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody List<WidgetReorderRequest> orders) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                profileWidgetOrchestrator.reorderWidgets(principal.getId(), orders)));
    }

    @PutMapping("/game-mode")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Boolean>> toggleGameMode(
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                profileWidgetOrchestrator.toggleGameMode(principal.getId())));
    }

}
