package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogIdAndTitleInternal {
    Long id;
    String title;
}
