package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.User;
import top.blogapi.service.discovery.DiscoveryService;

import java.util.Optional;

/**
 * Orchestrates content discovery features: random blog, random user, and category-based discovery.
 */
@Component
@RequiredArgsConstructor
public class DiscoveryOrchestrator {

    private final DiscoveryService discoveryService;

    @Transactional(readOnly = true)
    public Optional<Blog> getRandomBlog() {
        return discoveryService.getRandomBlog();
    }

    @Transactional(readOnly = true)
    public Optional<User> getRandomUser() {
        return discoveryService.getRandomUser();
    }

    @Transactional(readOnly = true)
    public Object discover(String category) {
        return discoveryService.discover(category);
    }
}
