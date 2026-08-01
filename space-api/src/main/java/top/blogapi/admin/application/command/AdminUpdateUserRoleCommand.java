package top.blogapi.admin.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.admin.domain.repository.AdminUserRepository;

@Service
@RequiredArgsConstructor
public class AdminUpdateUserRoleCommand {

    private final AdminUserRepository adminUserRepository;

    @Transactional
    public void execute(Long userId, String role) {
        adminUserRepository.updateRole(userId, role);
    }
}
