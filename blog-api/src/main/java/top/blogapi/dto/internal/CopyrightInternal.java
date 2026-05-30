package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CopyrightInternal {
    String title;
    String siteName;
}
