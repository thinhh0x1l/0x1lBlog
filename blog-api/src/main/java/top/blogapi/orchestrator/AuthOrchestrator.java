package top.blogapi.orchestrator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.request.auth.LoginRequest;
import top.blogapi.dto.request.auth.RegisterRequest;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.model.entity.User;
import top.blogapi.model.event.UserRegisteredEvent;
import top.blogapi.security.UserPrincipal;
import top.blogapi.security.auth.JwtService;
import top.blogapi.service.auth.RefreshTokenService;
import top.blogapi.service.user.UserService;

/**
 * Orchestrates authentication flows including registration, login, and token refresh.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthOrchestrator {

    UserService userService;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    ApplicationEventPublisher eventPublisher;
    RefreshTokenService refreshTokenService;
    UserMapper userMapper;

    @Transactional
    public AuthResult register(RegisterRequest request, String ipAddress) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName() != null ? request.getDisplayName() : request.getEmail());
        user.setRole("USER");
        user.setIsCreator(false);
        user.setStatus("ACTIVE");
        user = userService.create(user);

        eventPublisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getEmail()));

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        refreshTokenService.persistRefreshToken(refreshToken, user.getId(), ipAddress);

        UserResponse userResp = userMapper.toResponse(user);
        return new AuthResult(accessToken, refreshToken, userResp);
    }

    public AuthResult login(LoginRequest request, String ipAddress) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.updateLastActive(principal.getId());

        String accessToken = jwtService.generateAccessToken(principal.getId(), principal.getRole());
        String refreshToken = jwtService.generateRefreshToken(principal.getId());
        refreshTokenService.persistRefreshToken(refreshToken, principal.getId(), ipAddress);

        UserResponse userResp = userMapper.toResponse(principal.getUser());
        return new AuthResult(accessToken, refreshToken, userResp);
    }

    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        String role = jwtService.getRoleFromToken(refreshToken);
        return jwtService.generateAccessToken(userId, role);
    }

}
