package top.blogapi.service.discovery;

import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.User;

import java.util.Optional;

/**
 * Giao diện service khám phá nội dung, cung cấp đề xuất blog và người dùng
 * ngẫu nhiên cùng khám phá theo danh mục.
 */
public interface DiscoveryService {
    Optional<Blog> getRandomBlog();
    Optional<User> getRandomUser();
    Object discover(String category);
}
