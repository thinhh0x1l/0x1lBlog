package top.blogapi.service.dashboard.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CommentRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.dashboard.DashboardService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalBlogs() {
        return blogRepository.countPublished();
    }

    @Override
    public long getTotalComments() {
        return 0;
    }

    @Override
    public long getTotalViews() {
        return 0;
    }

    @Override
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("users", getTotalUsers());
        stats.put("blogs", getTotalBlogs());
        stats.put("comments", getTotalComments());
        stats.put("views", getTotalViews());
        return stats;
    }
}
