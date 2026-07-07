package top.blogapi.dto.response;

import lombok.Builder;
import lombok.Data;
import top.blogapi.model.entity.User;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho hồ sơ người dùng với thống kê và siêu dữ liệu.
 */
@Data
@Builder
public class UserResponse {
    private Long id;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private String website;
    private String location;
    private String role;
    private Boolean isCreator;
    private Integer blogCount;
    private Integer followerCount;
    private Integer followingCount;
    private Integer level;
    private OffsetDateTime createdAt;

    public static UserResponse from(User user ) {
        return UserResponse.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .website(user.getWebsite())
                .location(user.getLocation())
                .role(user.getRole())
                .isCreator(user.getIsCreator())
                .blogCount(user.getBlogCount())
                .followerCount(user.getFollowerCount())
                .followingCount(user.getFollowingCount())
                .level(user.getLevel())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
