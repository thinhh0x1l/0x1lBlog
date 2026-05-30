package top.blogapi.dto.response.blog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response.category.CategorySlug;
import top.blogapi.dto.response.tag.TagSlugs;
import top.blogapi.model.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogInfo{
    Long id;
    String title;
    String description;
    LocalDateTime createTime;
    Long views;
    Integer words;
    Integer readTime;
    Boolean top;
    CategorySlug category;
    List<TagSlugs> tags;
}