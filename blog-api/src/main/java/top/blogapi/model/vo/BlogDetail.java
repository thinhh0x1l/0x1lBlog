package top.blogapi.model.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response._common.MusicInfo;
import top.blogapi.model.entity.Category;
import top.blogapi.model.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogDetail {
    Long id;
    String title;
    String content;
    Boolean appreciation;
    Boolean commentEnabled;
    Boolean top;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    Integer views;
    Integer words;
    Integer readTime;
    String musicId;
    Category category;
    List<Tag> tags = new ArrayList<>();
    MusicInfo musicInfo;

}
