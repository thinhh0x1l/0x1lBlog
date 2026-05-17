package top.blogapi.model.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response._common.MusicInfo;
import top.blogapi.model.entity.Category;
import top.blogapi.model.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BlogDetail {
    Long id;
    String title;
    String content;
    Boolean appreciation;
    Boolean commentEnabled;
    Boolean top;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    Long views;
    Integer words;
    Integer readTime;
    String musicId;
    Category category;
    List<Tag> tags = new ArrayList<>();

    public static BlogDetail cloneBlogDetail(BlogDetail cache, Long views) {
        if (cache == null) return null;

        return BlogDetail.builder()
                .id(cache.id)
                .title(cache.title)
                .content(cache.content)
                .appreciation(cache.appreciation)
                .commentEnabled(cache.commentEnabled)
                .top(cache.top)
                .createTime(cache.createTime)
                .updateTime(cache.updateTime)
                .views(views)
                .words(cache.words)
                .readTime(cache.readTime)
                .musicId(cache.musicId)
                .category(cache.category)
                .tags(cache.tags != null ? new ArrayList<>(cache.tags) : new ArrayList<>())
                .build();
    }
}
