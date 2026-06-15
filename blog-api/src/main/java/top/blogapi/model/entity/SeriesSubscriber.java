package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeriesSubscriber {
    Long id;
    Long seriesId;
    Long userId;
    LocalDateTime createdAt;
}
