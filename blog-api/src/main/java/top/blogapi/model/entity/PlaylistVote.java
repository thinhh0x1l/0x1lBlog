package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bình chọn (tán thành/phản đối) của người dùng cho bài hát trong danh sách phát. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistVote {
    Long id;
    Long playlistId;
    Long songId;
    Long userId;
    int vote;
    OffsetDateTime createdAt;
}
