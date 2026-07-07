package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO phản hồi cho cập nhật trạng thái, kèm tùy chọn bình chọn với số phiếu.
 */
@Data
public class StatusResponse {
    private Long id;
    private Long userId;
    private Long threadId;
    private Integer partOrder;
    private String content;
    private String imageUrl;
    private String visibility;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PollResponse poll;

    @Data
    public static class PollResponse {
        private Long id;
        private String question;
        private List<String> options;
        private OffsetDateTime endsAt;
        private Map<Integer, Long> voteCounts;
        private boolean voted;
    }
}
