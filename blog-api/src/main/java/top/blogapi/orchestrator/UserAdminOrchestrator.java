package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.model.enums.UserRole;
import top.blogapi.model.enums.UserStatus;
import top.blogapi.service.user.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAdminOrchestrator {

    private final UserService userService;
    private final UserMapper userMapper;

    public List<UserResponse> findAll(int page, int size) {
        return userService.findAll(page, size).stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public void updateRole(Long id, UserRole role) {
        var user = userService.findById(id);
        user.setRole(role);
        userService.update(user);
    }

    public void banUser(Long id) {
        var user = userService.findById(id);
        user.setStatus(UserStatus.BANNED);
        userService.update(user);
    }
}
