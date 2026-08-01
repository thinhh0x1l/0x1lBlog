package top.blogapi.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.blogapi.engagement.follow.domain.event.FollowEvent;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.user.core.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * Lắng nghe sự kiện theo dõi/hủy theo dõi, ghi nhật ký thay đổi quan hệ người theo dõi.
 */
public class FollowEventListener {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFollow(FollowEvent event) {
        if ("FOLLOW".equals(event.getAction())) {
            log.info("User {} followed user {}", event.getFollowerId(), event.getFollowingId());
        } else if ("UNFOLLOW".equals(event.getAction())) {
            log.info("User {} unfollowed user {}", event.getFollowerId(), event.getFollowingId());
        }
    }
}
