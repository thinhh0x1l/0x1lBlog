package top.blogapi.user.auth.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import top.blogapi.infra.security.UserPrincipal;
import top.blogapi.infra.security.jwt.JwtService;
import top.blogapi.user.auth.application.AuthResult;
import top.blogapi.user.auth.domain.service.RefreshTokenService;
import top.blogapi.user.core.service.UserService;
import top.blogapi.user.auth.interfaces.dto.LoginRequest;
import top.blogapi.user.auth.interfaces.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class LoginCommand {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public AuthResult execute(LoginRequest request, String ipAddress) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.updateLastActive(principal.getId());

        String accessToken = jwtService.generateAccessToken(principal.getId(), principal.getRole());
        String refreshToken = jwtService.generateRefreshToken(principal.getId());
        refreshTokenService.persistRefreshToken(refreshToken, principal.getId(), ipAddress);

        return new AuthResult(accessToken, refreshToken, userMapper.toResponse(principal.getUser()));
    }
}
