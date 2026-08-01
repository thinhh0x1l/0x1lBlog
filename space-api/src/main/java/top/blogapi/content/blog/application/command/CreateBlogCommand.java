package top.blogapi.content.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.service.BlogService;
import top.blogapi.content.blog.interfaces.dto.CreateBlogRequest;

@Service
@RequiredArgsConstructor
public class CreateBlogCommand {

    private final BlogService blogService;

    @Transactional
    public Blog execute(CreateBlogRequest request, Long authorId) {
        Blog blog = new Blog();
        blog.setAuthorId(authorId);
        blog.setTitle(request.title());
        blog.setContent(request.content());
        blog.setDescription(request.description());
        blog.setCoverImage(request.coverImage());
        blog.setSlug(request.title().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        blog.setCategoryId(request.categoryId());
        blog.setContentType(request.contentType() != null ? request.contentType() : "MARKDOWN");
        blog.setStatus("DRAFT");
        blog.setVisibility("PUBLIC");
        blog.setAllowComments(true);
        blog = blogService.create(blog);

        if (request.hashtags() != null && !request.hashtags().isEmpty()) {
            blogService.linkHashtags(blog.getId(), request.hashtags());
        }

        return blog;
    }
}
