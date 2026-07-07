package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Bản sao lưu trữ của story đã hết hạn, giữ lại cho tác giả. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryArchive {
    Long id;
    Long userId;
    Long storyId;
    String mediaUrl;
    String mediaType;
    String textContent;
    Long viewCount;
    OffsetDateTime createdAt;
    OffsetDateTime archivedAt;
}
