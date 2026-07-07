package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Nội dung trang tĩnh như giới thiệu, điều khoản hoặc thông tin riêng tư. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutInfo {
    Long id;
    String content;
    String type;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
