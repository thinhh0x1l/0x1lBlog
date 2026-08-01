package top.blogapi.admin.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.user.core.service.UserService;

@Service
@RequiredArgsConstructor
public class BanUserCommand {

    private final UserService userService;

    public void banUser(Long userId) {
        userService.banUser(userId);
    }

    public void updateRole(Long userId, String role) {
        userService.updateRole(userId, role);
    }
}
