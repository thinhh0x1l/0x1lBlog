package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CommentResponse {
    private Long id;
    private Long blogId;
    private Long parentId;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private String guestName;
    private String content;
    private String status;
    private Integer likeCount;
    private Integer replyCount;
    private OffsetDateTime createdAt;
}
