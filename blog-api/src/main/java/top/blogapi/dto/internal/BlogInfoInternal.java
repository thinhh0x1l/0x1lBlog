package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;
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
public class BlogInfoInternal {
    Long id;
    String title;           // Tiêu đề bài viết
    String description;     // Mô tả
    LocalDateTime createTime;        // Thời gian tạo
    Integer views;          // Số lần xem
    Integer words;          // Số từ trong bài viết
    Integer readTime;       // Thời gian đọc (phút)
    Boolean top;
    Category category;      // Danh mục bài viết
    List<Tag> tags = new ArrayList<>();  // Thẻ (tag) bài viết
}
