package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Danh sách nhạc do người dùng tạo và sở hữu. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Playlist {
    Long id;
    Long ownerId;
    String title;
    Boolean isPublic;
    int songCount;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
