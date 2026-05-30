package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults( level = AccessLevel.PRIVATE)
@ToString
public class CategoryBlogCountInternal {
    Long id;
    String name;
    Long value;
}
