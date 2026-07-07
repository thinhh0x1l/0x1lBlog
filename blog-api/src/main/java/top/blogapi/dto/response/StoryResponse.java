package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho story với nội dung đa phương tiện, chế độ hiển thị và thời gian hết hạn.
 */
@Data
public class StoryResponse {
    private Long id;
    private Long userId;
    private String mediaUrl;
    private String mediaType;
    private String textContent;
    private String visibility;
    private Long viewCount;
    private OffsetDateTime expiresAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime archivedAt;
}
