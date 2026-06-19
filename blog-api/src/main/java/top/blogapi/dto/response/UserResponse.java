package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
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
}
