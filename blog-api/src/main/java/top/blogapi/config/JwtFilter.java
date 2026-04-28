package top.blogapi.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import top.blogapi.exception.ApiErrorResponse;
import top.blogapi.util.JwtUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    public JwtFilter(ObjectMapper objectMapper, JwtUtils jwtUtils) {
        this.objectMapper = objectMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            try{
                Claims claims = jwtUtils.getTokenContent(jwtToken);
                String username = claims.getSubject();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }catch (Exception e) {
                handleAuthenticationError(response, request,
                        "Thông tin đăng nhập đã hết hạn, vui lòng đăng nhập lại!",403);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private void handleAuthenticationError(HttpServletResponse response,HttpServletRequest request,
                                           String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=utf-8");
        ApiErrorResponse error = new ApiErrorResponse(
                "UNAUTHORIZED",
                message,
                status,
                request.getRequestURI(),
                request.getHeader("X-Trace-Id"),
                LocalDateTime.now(),
                Map.of("reason",message)
        );
        objectMapper.writeValue(response.getWriter(), error);
    }
}
