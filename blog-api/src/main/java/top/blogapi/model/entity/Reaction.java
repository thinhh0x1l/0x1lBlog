package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Cảm xúc chung cho bất kỳ loại đối tượng nào được hỗ trợ (blog, bình luận, v.v.). */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {
    Long id;
    Long userId;
    String targetType;
    Long targetId;
    String type;
    OffsetDateTime createdAt;
}
