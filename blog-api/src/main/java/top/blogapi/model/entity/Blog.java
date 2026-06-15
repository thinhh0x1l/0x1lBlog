package top.blogapi.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Blog extends BaseEntity {
    String title;
    String content;
    String description;
    Boolean published;
    Boolean recommend;
    Boolean appreciation;
    Boolean commentEnabled;
    Boolean top;
    Integer views;
    Integer words;
    Integer readTime;
    String musicId;
    User user;
    Category category;
    List<Tag> tags = new ArrayList<>();

}
