package top.blogapi.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.blogapi.model.event.FollowEvent;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowEventListener {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    @EventListener
    public void handleFollow(FollowEvent event) {
        if ("FOLLOW".equals(event.getAction())) {
            log.info("User {} followed user {}", event.getFollowerId(), event.getFollowingId());
        } else if ("UNFOLLOW".equals(event.getAction())) {
            log.info("User {} unfollowed user {}", event.getFollowerId(), event.getFollowingId());
        }
    }
}
