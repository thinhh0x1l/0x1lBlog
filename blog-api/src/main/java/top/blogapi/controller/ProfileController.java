package top.blogapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.profile.ProfileLayoutDTO;
import top.blogapi.orchestrator.ProfileOrchestrator;
import top.blogapi.orchestrator.ProfileWidgetOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Endpoint để xem và cập nhật hồ sơ người dùng và bố cục hồ sơ.
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    @Data
    public static class UpdateProfileRequest {
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        private String displayName;

        @Size(max = 1000, message = "Bio must not exceed 1000 characters")
        private String bio;

        @Size(max = 200, message = "Website must not exceed 200 characters")
        private String website;

        @Size(max = 200, message = "Location must not exceed 200 characters")
        private String location;

        @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
        private String avatarUrl;
    }

    private final ProfileOrchestrator profileOrchestrator;
    private final ProfileWidgetOrchestrator profileWidgetOrchestrator;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(profileOrchestrator.getProfile(principal.getId())));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                     @Valid @RequestBody UpdateProfileRequest update) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var user = new top.blogapi.model.entity.User();
        user.setDisplayName(update.getDisplayName());
        user.setBio(update.getBio());
        user.setWebsite(update.getWebsite());
        user.setLocation(update.getLocation());
        user.setAvatarUrl(update.getAvatarUrl());
        return ResponseEntity.ok(ApiResponse.success(profileOrchestrator.updateProfile(principal.getId(), user)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getPublicProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(profileOrchestrator.getProfile(userId)));
    }

    @GetMapping("/{userId}/layout")
    public ResponseEntity<ApiResponse<ProfileLayoutDTO>> getProfileLayout(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(profileWidgetOrchestrator.getProfileLayout(userId)));
    }
}
