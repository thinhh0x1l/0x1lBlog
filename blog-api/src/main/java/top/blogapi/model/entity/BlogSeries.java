package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogSeries {
    Long id;
    String name;
    String description;
    String coverUrl;
    Long authorId;
    boolean published;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
