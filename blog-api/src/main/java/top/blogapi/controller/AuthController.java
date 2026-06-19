package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.request.auth.LoginRequest;
import top.blogapi.dto.request.auth.RefreshTokenRequest;
import top.blogapi.dto.request.auth.RegisterRequest;
import top.blogapi.orchestrator.AuthOrchestrator;
import top.blogapi.service.auth.JwtService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthOrchestrator authOrchestrator;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authOrchestrator.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authOrchestrator.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestBody RefreshTokenRequest request) {
        Long userId = jwtService.getUserIdFromToken(request.getRefreshToken());
        var user = jwtService.getRoleFromToken(request.getRefreshToken());
        String token = jwtService.generateAccessToken(userId, user);
        return ResponseEntity.ok(ApiResponse.success(token));
    }
}
