package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Cấu hình trang web dạng key-value. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiteSetting {
    Long id;
    String key;
    String value;
    String type;
    String description;
    OffsetDateTime updatedAt;
}
