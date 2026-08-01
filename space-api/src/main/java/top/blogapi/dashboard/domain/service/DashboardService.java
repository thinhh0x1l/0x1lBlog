package top.blogapi.dashboard.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.content.blog.domain.repository.BlogRepository;
import top.blogapi.engagement.comment.domain.repository.CommentRepository;
import top.blogapi.user.core.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalBlogs() {
        return blogRepository.countPublished();
    }

    public long getTotalComments() {
        return 0;
    }

    public long getTotalViews() {
        return 0;
    }

    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("users", getTotalUsers());
        stats.put("blogs", getTotalBlogs());
        stats.put("comments", getTotalComments());
        stats.put("views", getTotalViews());
        return stats;
    }
}
