package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class About {
    Long id;
    String nameEn;
    String nameVn;
    String value;
}
