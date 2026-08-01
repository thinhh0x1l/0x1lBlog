package top.blogapi.content.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.blog.domain.event.BlogPublishedEvent;
import top.blogapi.content.blog.domain.service.BlogService;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class PublishBlogCommand {

    private final BlogService blogService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Blog execute(Long id, Long userId) {
        Blog blog = blogService.findById(id);
        if (!blog.getAuthorId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Bạn không có quyền xuất bản bài viết này");
        }
        blogService.publish(id);
        Blog published = blogService.findById(id);
        eventPublisher.publishEvent(new BlogPublishedEvent(published.getId(), published.getAuthorId()));
        return published;
    }
}
