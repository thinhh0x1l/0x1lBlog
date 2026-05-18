package top.blogapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.blogapi.model.entity.Guest;
import top.blogapi.service.GuestService;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@Component
public class GuestTokenFilter extends OncePerRequestFilter {
    private final SecureRandom secureRandom = new SecureRandom();
    private static final String COOKIE_NAME = "guest_token";
    private final GuestService guestService;

    public GuestTokenFilter(GuestService guestService) {
        this.guestService = guestService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String guestToken = extractFromCookie(request);
        if(guestToken == null)
            guestToken = createGuessToken(response);

        request.setAttribute("guestToken", guestToken);
        filterChain.doFilter(request,response);
    }

    private String extractFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String createGuessToken(HttpServletResponse response){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(365*100))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return token;
    }
}
