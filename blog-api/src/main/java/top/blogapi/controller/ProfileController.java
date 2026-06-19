package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.model.entity.User;
import top.blogapi.repository.UserRepository;
import top.blogapi.security.UserPrincipal;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(userMapper.toResponse(user)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                     @RequestBody User update) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDisplayName(update.getDisplayName());
        user.setBio(update.getBio());
        user.setWebsite(update.getWebsite());
        user.setLocation(update.getLocation());
        user.setAvatarUrl(update.getAvatarUrl());
        userRepository.update(user);
        return ResponseEntity.ok(ApiResponse.success(userMapper.toResponse(user)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getPublicProfile(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success(userMapper.toResponse(user)));
    }
}
