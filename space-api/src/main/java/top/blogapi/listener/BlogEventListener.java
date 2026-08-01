package top.blogapi.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.blogapi.content.blog.domain.event.BlogPublishedEvent;
import top.blogapi.user.core.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * Lắng nghe sự kiện blog, làm mới bộ đếm bài viết của tác giả
 * khi blog được xuất bản.
 */
public class BlogEventListener {

    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBlogPublished(BlogPublishedEvent event) {
        userRepository.refreshBlogCount(event.getAuthorId());
        log.info("Blog published: id={}, authorId={}", event.getBlogId(), event.getAuthorId());
    }
}
