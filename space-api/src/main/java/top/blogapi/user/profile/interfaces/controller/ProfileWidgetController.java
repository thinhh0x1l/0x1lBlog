package top.blogapi.user.profile.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.user.profile.interfaces.dto.WidgetDTO;
import top.blogapi.user.profile.interfaces.dto.WidgetReorderRequest;
import top.blogapi.user.profile.interfaces.dto.WidgetUpdateRequest;
import top.blogapi.user.profile.application.query.GetWidgetsQuery;
import top.blogapi.user.profile.application.command.UpdateWidgetCommand;
import top.blogapi.user.profile.application.command.ReorderWidgetsCommand;
import top.blogapi.infra.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/profile/widgets")
@RequiredArgsConstructor
public class ProfileWidgetController {

    private final GetWidgetsQuery getWidgetsQuery;
    private final UpdateWidgetCommand updateWidgetCommand;
    private final ReorderWidgetsCommand reorderWidgetsCommand;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<WidgetDTO>>> getWidgets(
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                getWidgetsQuery.execute(principal.getId())));
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
                updateWidgetCommand.execute(principal.getId(), request)));
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
                updateWidgetCommand.toggleWidget(principal.getId(), widgetType)));
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
                reorderWidgetsCommand.execute(principal.getId(), orders)));
    }

    @PutMapping("/game-mode")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Boolean>> toggleGameMode(
            @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(
                updateWidgetCommand.toggleGameMode(principal.getId())));
    }
}
