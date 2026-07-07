package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Canvas pixel-art cá nhân hoặc cộng tác với kích thước và thời gian xác định. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Canvas {
    Long id;
    String type;
    String title;
    int width;
    int height;
    Long ownerId;
    OffsetDateTime startsAt;
    OffsetDateTime endsAt;
    Boolean isActive;
    OffsetDateTime createdAt;
}
