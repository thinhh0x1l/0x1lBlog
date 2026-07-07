package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bài hát trong danh sách phát, được thêm bởi người dùng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistSong {
    Long id;
    Long playlistId;
    Long addedBy;
    String title;
    String artist;
    String source;
    String sourceId;
    String thumbnailUrl;
    Integer durationSec;
    Integer sortOrder;
    int voteCount;
    OffsetDateTime createdAt;
}
