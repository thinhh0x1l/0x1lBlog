package top.blogapi.model.entity.profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Widget có thể cấu hình hiển thị trên trang hồ sơ người dùng. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileWidget {
    Long id;
    Long userId;
    String widgetType;
    Boolean isVisible;
    Integer sortOrder;
    String config;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
