package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Quan hệ theo dõi giữa hai người dùng (người theo dõi và được theo dõi). */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Follow {
    Long id;
    Long followerId;
    Long followingId;
    OffsetDateTime createdAt;
}
