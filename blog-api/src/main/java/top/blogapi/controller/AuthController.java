package top.blogapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.request.auth.LoginRequest;
import top.blogapi.dto.request.auth.RefreshTokenRequest;
import top.blogapi.dto.request.auth.RegisterRequest;
import top.blogapi.dto.response.AuthResponse;
import top.blogapi.orchestrator.AuthOrchestrator;
import top.blogapi.orchestrator.AuthResult;

/**
 * Xử lý xác thực người dùng: đăng ký, đăng nhập và làm mới token.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthOrchestrator authOrchestrator;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request,
                                                               HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        AuthResult result = authOrchestrator.register(request, ip);
        AuthResponse response = new AuthResponse();
        response.setAccessToken(result.getAccessToken());
        response.setRefreshToken(result.getRefreshToken());
        response.setTokenType("Bearer");
        response.setUser(result.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
                                                            HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        AuthResult result = authOrchestrator.login(request, ip);
        AuthResponse response = new AuthResponse();
        response.setAccessToken(result.getAccessToken());
        response.setRefreshToken(result.getRefreshToken());
        response.setTokenType("Bearer");
        response.setUser(result.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String token = authOrchestrator.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(token));
    }
}
