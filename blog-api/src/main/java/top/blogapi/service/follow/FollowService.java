package top.blogapi.service.follow;

import top.blogapi.model.entity.Follow;

import java.util.List;

/**
 * Giao diện service cho thao tác theo dõi/hủy theo dõi, quản lý quan hệ
 * người theo dõi và cung cấp danh sách phân trang.
 */
public interface FollowService {
    void follow(Long followerId, Long followingId);
    void unfollow(Long followerId, Long followingId);
    boolean isFollowing(Long followerId, Long followingId);
    List<Follow> getFollowers(Long userId, int page, int size);
    List<Follow> getFollowing(Long userId, int page, int size);
    long countFollowers(Long userId);
    long countFollowing(Long userId);
}
