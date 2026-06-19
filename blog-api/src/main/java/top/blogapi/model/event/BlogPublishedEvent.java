package top.blogapi.model.event;

import lombok.Value;
import top.blogapi.model.entity.Blog;

@Value
public class BlogPublishedEvent {
    Blog blog;
}
