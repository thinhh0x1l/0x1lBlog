package top.blogapi.dto.internal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults( level = AccessLevel.PRIVATE)
public class SearchBlog {
    Long id;
    String title;
    String content;
}
