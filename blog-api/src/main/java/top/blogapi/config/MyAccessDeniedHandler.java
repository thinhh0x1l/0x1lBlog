package top.blogapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.blogapi.common.response.ApiResponse;

import java.io.IOException;

/**
 * Trả về phản hồi JSON 403 Forbidden khi người dùng không có quyền truy cập.
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> result = ApiResponse.error("Bạn không có quyền truy cập!");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
