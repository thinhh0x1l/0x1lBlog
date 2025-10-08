package top.blogapi.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import top.blogapi.util.Result;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;

    public JwtFilter(SecretKey secretKey, ObjectMapper objectMapper) {
        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            try{
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();
                String username = claims.getSubject();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }catch (Exception e) {
                handleAuthenticationError(response,"Thông tin đăng nhập đã hết hạn, vui lòng đăng nhập lại!",403);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private void handleAuthenticationError(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=utf-8");

        Result result = Result.create(status, message);
        objectMapper.writeValue(response.getWriter(), result);
    }
}
