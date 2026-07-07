package top.blogapi.model.event;

import lombok.Value;
import top.blogapi.model.entity.Blog;

/** Sự kiện được kích hoạt khi bài viết blog được xuất bản. */
@Value
public class BlogPublishedEvent {
    Blog blog;
}
