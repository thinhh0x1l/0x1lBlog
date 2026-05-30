package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntroductionInternal {
    String avatar;
    String name;
    String leetCode;
    String facebook;
    String instagram;
    String github;
    String email;

    List<String> rollText = new ArrayList<>();
    List<FavoriteInternal> favorites = new ArrayList<>();
}
