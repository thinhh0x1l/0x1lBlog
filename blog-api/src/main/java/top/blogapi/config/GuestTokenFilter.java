package top.blogapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.blogapi.service.GuestService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
public class GuestTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "x-guest-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String guestToken = request.getHeader(TOKEN_HEADER);

        if (guestToken == null || guestToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute("guestToken", guestToken);

        filterChain.doFilter(request, response);
    }
}