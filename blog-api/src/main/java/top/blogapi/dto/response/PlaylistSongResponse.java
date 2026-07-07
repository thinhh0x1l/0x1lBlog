package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * DTO phản hồi cho bài hát trong danh sách phát, bao gồm thông tin bình chọn và thứ tự.
 */
@Data
public class PlaylistSongResponse {
    private Long id;
    private Long addedBy;
    private String title;
    private String artist;
    private String source;
    private String sourceId;
    private String thumbnailUrl;
    private Integer durationSec;
    private Integer sortOrder;
    private int voteCount;
    private OffsetDateTime createdAt;
}
