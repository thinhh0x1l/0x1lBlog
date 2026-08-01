package top.blogapi.user.auth.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.infra.security.jwt.JwtService;
import top.blogapi.user.auth.application.AuthResult;
import top.blogapi.user.core.entity.User;
import top.blogapi.user.core.event.UserRegisteredEvent;
import top.blogapi.user.auth.domain.service.RefreshTokenService;
import top.blogapi.user.core.service.UserService;
import top.blogapi.user.auth.interfaces.dto.RegisterRequest;
import top.blogapi.user.auth.interfaces.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class RegisterCommand {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResult execute(RegisterRequest request, String ipAddress) {
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName() != null ? request.displayName() : request.email());
        user.setRole("USER");
        user.setIsCreator(false);
        user.setStatus("ACTIVE");
        user = userService.create(user);

        eventPublisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getEmail()));

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        refreshTokenService.persistRefreshToken(refreshToken, user.getId(), ipAddress);

        return new AuthResult(accessToken, refreshToken, userMapper.toResponse(user));
    }
}
