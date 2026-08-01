package top.blogapi.admin.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.user.core.service.UserService;

@Service
@RequiredArgsConstructor
public class GetUserQuery {

    private final UserService userService;

    public Object execute(int page, int size) {
        return userService.findAll(page, size);
    }
}
