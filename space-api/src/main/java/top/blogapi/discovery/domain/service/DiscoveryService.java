package top.blogapi.discovery.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.content.blog.domain.entity.Blog;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.user.core.entity.User;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.content.category.domain.repository.CategoryRepository;
import top.blogapi.user.core.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public Optional<Blog> getRandomBlog() {
        return blogRepository.findRandomPublished();
    }

    public Optional<User> getRandomUser() {
        return userRepository.findRandomActive();
    }

    public Object discover(String category) {
        if ("blog".equalsIgnoreCase(category)) {
            return getRandomBlog().orElse(null);
        }
        if ("user".equalsIgnoreCase(category)) {
            return getRandomUser().orElse(null);
        }
        Category cat = categoryRepository.findBySlug(category)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return blogRepository.findRandomByCategoryId(cat.getId()).orElse(null);
    }
}
