package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho canvas cộng tác với kích thước và thời gian.
 */
@Data
public class CanvasResponse {
    private Long id;
    private String type;
    private String title;
    private int width;
    private int height;
    private Long ownerId;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
