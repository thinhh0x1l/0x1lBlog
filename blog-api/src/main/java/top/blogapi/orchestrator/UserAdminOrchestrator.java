package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.service.user.UserService;

import java.util.List;

/**
 * Orchestrates admin user management: listing, role updates, and account banning.
 */
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

    @Transactional
    public void updateRole(Long id, String role) {
        var user = userService.findById(id);
        user.setRole(role);
        userService.update(user);
    }

    @Transactional
    public void banUser(Long id) {
        var user = userService.findById(id);
        user.setStatus("BANNED");
        userService.update(user);
    }
}
