package top.blogapi.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO phản hồi cho danh sách phát với các bài hát và siêu dữ liệu.
 */
@Data
public class PlaylistResponse {
    private Long id;
    private Long ownerId;
    private String title;
    private Boolean isPublic;
    private int songCount;
    private List<PlaylistSongResponse> songs;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
