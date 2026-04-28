package top.blogapi.service.impl.orchestration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.User;
import top.blogapi.model.vo.LoginInfo;
import top.blogapi.service.impl.UserServiceImpl;
import top.blogapi.util.JwtUtils;

import java.util.Map;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LoginOrchestrator {
    UserServiceImpl userService;
    JwtUtils jwtUtils;
    public Map<String, Object> handleLogin(LoginInfo loginInfo){
        User user = (User) userService.loadUserByUsername(loginInfo.getUsername());
        user.setPassword(null);
        String jwt = jwtUtils.generateToken(user.getUsername());
        return Map.of(
                "token", jwt,
                "user", user
        );
    }
}
