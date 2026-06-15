package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Badge {
    Long id;
    String name;
    String displayName;
    String description;
    String iconUrl;
    String tier;
    LocalDateTime createdAt;
}
