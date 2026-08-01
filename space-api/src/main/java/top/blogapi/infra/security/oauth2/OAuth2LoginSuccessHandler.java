package top.blogapi.infra.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.blogapi.infra.security.jwt.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${app.frontend-url:http://localhost:5174}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long userId = (Long) oAuth2User.getAttributes().get("userId");
        String role = (String) oAuth2User.getAttributes().get("userRole");

        String token = jwtService.generateAccessToken(userId, role);
        String email = (String) oAuth2User.getAttributes().get("email");

        log.info("OAuth2 login success: userId={}, email={}", userId, email);

        String redirectUrl = String.format("%s/oauth2/callback?token=%s&userId=%d&email=%s",
                frontendUrl, token, userId, email);
        response.sendRedirect(redirectUrl);
    }
}
