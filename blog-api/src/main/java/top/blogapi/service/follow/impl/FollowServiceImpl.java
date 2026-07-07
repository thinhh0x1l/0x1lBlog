package top.blogapi.service.follow.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Follow;
import top.blogapi.repository.FollowRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.follow.FollowService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai FollowService với chống tự theo dõi và phát hiện theo dõi
 * trùng lặp, cung cấp danh sách người theo dõi phân trang.
 */
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
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

    @Override
    public void unfollow(Long followerId, Long followingId) {
        followRepository.delete(followerId, followingId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.exists(followerId, followingId);
    }

    @Override
    public List<Follow> getFollowers(Long userId, int page, int size) {
        return followRepository.findByFollowingId(userId, size, page * size);
    }

    @Override
    public List<Follow> getFollowing(Long userId, int page, int size) {
        return followRepository.findByFollowerId(userId, size, page * size);
    }

    @Override
    public long countFollowers(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    @Override
    public long countFollowing(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
}
