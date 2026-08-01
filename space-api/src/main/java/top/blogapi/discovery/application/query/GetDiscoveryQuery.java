package top.blogapi.discovery.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.user.core.entity.User;
import top.blogapi.discovery.domain.service.DiscoveryService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetDiscoveryQuery {

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
