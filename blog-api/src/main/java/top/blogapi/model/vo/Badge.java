package top.blogapi.model.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Badge {
    String title;
    String url;
    String subject;
    String value;
    String color;
}
