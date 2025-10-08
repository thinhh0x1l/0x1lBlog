package top.blogapi.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import top.blogapi.bean.Result;
import top.blogapi.bean.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;
    private final long expireTime;
    public JwtLoginFilter(String loginUrl,
                          AuthenticationManager authenticationManager,
                          SecretKey secretKey,
                          ObjectMapper objectMapper,
                          long expireTime) {
        super(authenticationManager);

        setFilterProcessesUrl(loginUrl);

        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
        this.expireTime = expireTime;

        setAuthenticationSuccessHandler(this::onAuthenticationSuccess);
        setAuthenticationFailureHandler(this::onAuthenticationFailure);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            throw new BadCredentialsException("Lỗi phương thức yêu cầu  : " + request.getMethod());
        }

        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());

            setDetails(request, authRequest);
            return getAuthenticationManager().authenticate(authRequest);

        } catch (IOException | java.io.IOException e) {
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }
    }
    private void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws IOException, java.io.IOException {

        String jwtToken = Jwts.builder()
                .subject(authentication.getName())
                .expiration(Date.from(Instant.now().plusSeconds(expireTime)))
                .signWith(secretKey)
                .compact();

        User user = (User) authentication.getPrincipal();
        user.setPassword(null);

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", jwtToken);

        Result result = Result.ok("Đăng nhập thành công", data);

        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), result);
    }

    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException exception) throws java.io.IOException {

        String errorMessage = getErrorMessage(exception);
        Result result = Result.create(401, errorMessage);

        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), result);
    }

    private String getErrorMessage(AuthenticationException exception) {
        return switch (exception) {
            case LockedException e -> "Tài khoản đã bị khóa, vui lòng liên hệ với quản trị viên!";
            case CredentialsExpiredException e -> "Mật khẩu đã hết hạn, vui lòng liên hệ với quản trị viên!";
            case AccountExpiredException e -> "Tài khoản đã hết hạn, vui lòng liên hệ với quản trị viên!";
            case DisabledException e -> "Tài khoản đã bị vô hiệu hóa, vui lòng liên hệ với quản trị viên!";
            case BadCredentialsException e -> "Tên người dùng hoặc mật khẩu sai!";
            default -> exception.getMessage();
        };
    }
}
