package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults( level = AccessLevel.PRIVATE)
@ToString
public class TagBlogCount {
    Long id;
    String name;
    String color;
    Long value;
}
