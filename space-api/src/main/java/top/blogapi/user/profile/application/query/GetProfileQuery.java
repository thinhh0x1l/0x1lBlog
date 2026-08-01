package top.blogapi.user.profile.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.user.core.service.UserService;
import top.blogapi.user.auth.interfaces.dto.UserMapper;
import top.blogapi.user.auth.interfaces.dto.UserResponse;

@Service
@RequiredArgsConstructor
public class GetProfileQuery {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserResponse execute(Long id) {
        return userMapper.toResponse(userService.findById(id));
    }
}
