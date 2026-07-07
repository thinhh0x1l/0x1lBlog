package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Cập nhật trạng thái dạng ngắn, có thể nằm trong luồng hội thoại. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Status extends BaseEntity {
    Long userId;
    Long threadId;
    Integer partOrder;
    String content;
    String imageUrl;
    String visibility;
}
