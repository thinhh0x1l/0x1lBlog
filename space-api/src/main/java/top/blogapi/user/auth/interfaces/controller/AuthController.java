package top.blogapi.user.auth.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.user.auth.interfaces.dto.LoginRequest;
import top.blogapi.user.auth.interfaces.dto.RefreshTokenRequest;
import top.blogapi.user.auth.interfaces.dto.RegisterRequest;
import top.blogapi.user.auth.interfaces.dto.AuthResponse;
import top.blogapi.user.auth.application.AuthResult;
import top.blogapi.user.auth.application.command.LoginCommand;
import top.blogapi.user.auth.application.command.RegisterCommand;
import top.blogapi.user.auth.application.command.RefreshTokenCommand;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginCommand loginCommand;
    private final RegisterCommand registerCommand;
    private final RefreshTokenCommand refreshTokenCommand;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request,
                                                               HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        AuthResult result = registerCommand.execute(request, ip);
        AuthResponse response = new AuthResponse(result.getAccessToken(), result.getRefreshToken(), "Bearer", result.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
                                                            HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        AuthResult result = loginCommand.execute(request, ip);
        AuthResponse response = new AuthResponse(result.getAccessToken(), result.getRefreshToken(), "Bearer", result.getUser());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String token = refreshTokenCommand.execute(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(token));
    }
}
