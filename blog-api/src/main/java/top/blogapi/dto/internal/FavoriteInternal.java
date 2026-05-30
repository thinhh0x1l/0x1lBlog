package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteInternal {
    String title;
    String content;
}
