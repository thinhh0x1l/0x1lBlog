package top.blogapi.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.blogapi.bean.Result;

import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Result result = Result.create(500,exception.getMessage());

        if(exception instanceof LockedException)
            result.setMsg("Tài khoản đã bị khóa, vui lòng liên hệ với quản trị viên!");
        else if (exception instanceof CredentialsExpiredException)
            result.setMsg("Mật khẩu đã hết hạn, vui lòng liên hệ với quản trị viên!");
        else if (exception instanceof AccountExpiredException)
            result.setMsg("Tài khoản đã hết hạn, vui lòng liên hệ với quản trị viên!");
        else if (exception instanceof DisabledException)
            result.setMsg("Tài khoản đã bị vô hiệu hóa, vui lòng liên hệ với quản trị viên!");
        else if(exception instanceof BadCredentialsException)
            result.setMsg("Tên người dùng hoặc mật khẩu không đúng!");

        response.getWriter().write(mapper.writeValueAsString(result));
    }
}
