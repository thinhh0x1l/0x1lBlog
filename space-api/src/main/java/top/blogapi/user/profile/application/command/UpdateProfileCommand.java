package top.blogapi.user.profile.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.user.core.entity.User;
import top.blogapi.user.core.service.UserService;
import top.blogapi.user.auth.interfaces.dto.UserMapper;
import top.blogapi.user.auth.interfaces.dto.UserResponse;
import top.blogapi.user.profile.interfaces.controller.ProfileController.UpdateProfileRequest;

@Service
@RequiredArgsConstructor
public class UpdateProfileCommand {

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse execute(Long id, UpdateProfileRequest request) {
        User user = userService.findById(id);
        if (request.getDisplayName() != null) user.setDisplayName(request.getDisplayName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getWebsite() != null) user.setWebsite(request.getWebsite());
        if (request.getLocation() != null) user.setLocation(request.getLocation());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        user = userService.update(user);
        return userMapper.toResponse(user);
    }
}
