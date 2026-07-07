package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho bình luận với thông tin tác giả và số lượng phản hồi.
 */
@Data
public class CommentResponse {
    private Long id;
    private String targetType;
    private Long targetId;
    private Long parentId;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private String content;
    private String status;
    private Integer replyCount;
    private OffsetDateTime createdAt;
}
