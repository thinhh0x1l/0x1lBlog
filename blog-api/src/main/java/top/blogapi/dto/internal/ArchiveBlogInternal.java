package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArchiveBlogInternal {
    Long id;
    String title;
    String day;
    String yM;
}
