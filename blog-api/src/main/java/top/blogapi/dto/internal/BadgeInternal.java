package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadgeInternal {
    String title;
    String url;
    String subject;
    String value;
    String color;
}
