package top.blogapi.model.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Copyright {
    String title;
    String siteName;
}
