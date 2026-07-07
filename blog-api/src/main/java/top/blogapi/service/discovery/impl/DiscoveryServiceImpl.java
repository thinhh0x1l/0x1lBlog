package top.blogapi.service.discovery.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.Category;
import top.blogapi.model.entity.User;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.discovery.DiscoveryService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai DiscoveryService cung cấp truy vấn khám phá nội dung
 * ngẫu nhiên theo blog, người dùng và danh mục.
 */
public class DiscoveryServiceImpl implements DiscoveryService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Blog> getRandomBlog() {
        return blogRepository.findRandomPublished();
    }

    @Override
    public Optional<User> getRandomUser() {
        return userRepository.findRandomActive();
    }

    @Override
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
