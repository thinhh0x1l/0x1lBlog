package top.blogapi.user.auth.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.infra.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommand {

    private final JwtService jwtService;

    public String execute(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        String role = jwtService.getRoleFromToken(refreshToken);
        return jwtService.generateAccessToken(userId, role);
    }
}
