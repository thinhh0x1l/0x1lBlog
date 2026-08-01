package top.blogapi.engagement.follow.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.engagement.follow.domain.entity.Follow;
import top.blogapi.engagement.follow.domain.repository.FollowRepository;
import top.blogapi.user.core.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new AppException(ErrorCode.INVALID_INPUT, "Cannot follow yourself");
        }
        if (followRepository.exists(followerId, followingId)) {
            throw new AppException(ErrorCode.DATA_CONFLICT, "Already following");
        }
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        followRepository.insert(follow);
    }

    public void unfollow(Long followerId, Long followingId) {
        followRepository.delete(followerId, followingId);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.exists(followerId, followingId);
    }

    public List<Follow> getFollowers(Long userId, int page, int size) {
        return followRepository.findByFollowingId(userId, size, page * size);
    }

    public List<Follow> getFollowing(Long userId, int page, int size) {
        return followRepository.findByFollowerId(userId, size, page * size);
    }

    public long countFollowers(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    public long countFollowing(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
}
