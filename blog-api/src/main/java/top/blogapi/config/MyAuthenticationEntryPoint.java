package top.blogapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.blogapi.common.response.ApiResponse;

import java.io.IOException;

/**
 * Trả về phản hồi JSON 401 Unauthorized khi yêu cầu không có xác thực hợp lệ.
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> result = ApiResponse.error("Vui lòng đăng nhập!");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
