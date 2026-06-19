package top.blogapi.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.blogapi.model.event.UserRegisteredEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("New user registered: username={}, email={}", event.getUsername(), event.getEmail());
    }
}
