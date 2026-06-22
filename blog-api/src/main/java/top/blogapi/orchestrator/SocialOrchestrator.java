package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.service.follow.FollowService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SocialOrchestrator {

    private final FollowService followService;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
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
