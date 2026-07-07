package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.model.entity.User;
import top.blogapi.service.user.UserService;

/**
 * Orchestrates profile retrieval and update with entity-to-DTO mapping.
 */
@Component
@RequiredArgsConstructor
public class ProfileOrchestrator {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserResponse getProfile(Long id) {
        return userMapper.toResponse(userService.findById(id));
    }

    @Transactional
    public UserResponse updateProfile(Long id, User update) {
        User user = userService.findById(id);
        if (update.getDisplayName() != null) user.setDisplayName(update.getDisplayName());
        if (update.getBio() != null) user.setBio(update.getBio());
        if (update.getWebsite() != null) user.setWebsite(update.getWebsite());
        if (update.getLocation() != null) user.setLocation(update.getLocation());
        if (update.getAvatarUrl() != null) user.setAvatarUrl(update.getAvatarUrl());
        user = userService.update(user);
        return userMapper.toResponse(user);
    }
}
