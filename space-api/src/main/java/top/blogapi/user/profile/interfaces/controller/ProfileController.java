package top.blogapi.user.profile.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.user.profile.interfaces.dto.ProfileLayoutDTO;
import top.blogapi.user.profile.application.query.GetProfileQuery;
import top.blogapi.user.profile.application.command.UpdateProfileCommand;
import top.blogapi.user.profile.application.query.GetWidgetsQuery;
import top.blogapi.infra.security.UserPrincipal;

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

    private final GetProfileQuery getProfileQuery;
    private final UpdateProfileCommand updateProfileCommand;
    private final GetWidgetsQuery getWidgetsQuery;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getProfileQuery.execute(principal.getId())));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                     @Valid @RequestBody UpdateProfileRequest update) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(updateProfileCommand.execute(principal.getId(), update)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getPublicProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(getProfileQuery.execute(userId)));
    }

    @GetMapping("/{userId}/layout")
    public ResponseEntity<ApiResponse<ProfileLayoutDTO>> getProfileLayout(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(getWidgetsQuery.getProfileLayout(userId)));
    }
}
