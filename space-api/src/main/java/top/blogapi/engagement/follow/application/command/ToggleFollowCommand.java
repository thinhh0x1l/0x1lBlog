package top.blogapi.engagement.follow.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.engagement.follow.domain.service.FollowService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToggleFollowCommand {

    private final FollowService followService;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        followService.follow(followerId, followingId);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        followService.unfollow(followerId, followingId);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followService.isFollowing(followerId, followingId);
    }

    public List<?> getFollowers(Long userId, int page, int size) {
        return followService.getFollowers(userId, page, size);
    }

    public List<?> getFollowing(Long userId, int page, int size) {
        return followService.getFollowing(userId, page, size);
    }
}
