package top.blogapi.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.event.BlogPublishedEvent;
import top.blogapi.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlogEventListener {

    private final UserRepository userRepository;

    @EventListener
    public void handleBlogPublished(BlogPublishedEvent event) {
        Blog blog = event.getBlog();
        userRepository.refreshBlogCount(blog.getAuthorId());
        log.info("Blog published: id={}, authorId={}", blog.getId(), blog.getAuthorId());
    }
}
