package top.blogapi.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.blogapi.user.core.event.UserRegisteredEvent;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * Lắng nghe sự kiện đăng ký người dùng, ghi nhật ký đăng ký mới.
 */
public class UserEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("New user registered: email={}", event.getEmail());
    }
}
