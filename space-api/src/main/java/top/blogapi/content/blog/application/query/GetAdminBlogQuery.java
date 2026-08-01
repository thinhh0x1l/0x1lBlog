package top.blogapi.content.blog.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.service.BlogService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAdminBlogQuery {

    private final BlogService blogService;

    public List<Blog> execute(int page, int size) {
        return blogService.getPublished(page, size);
    }
}
