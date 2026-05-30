package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogTagsInfoInternal {
    Long id;
    String title;           // Tiêu đề bài viết
    String description;     // Mô tả
    LocalDateTime createTime;        // Thời gian tạo
    Integer views;          // Số lần xem
    Integer words;          // Số từ trong bài viết
    Integer readTime;       // Thời gian đọc (phút)
    Boolean top;
    String allTagNames;
    String allTagColors;
    String categoryName;      // Danh mục bài viết
}
