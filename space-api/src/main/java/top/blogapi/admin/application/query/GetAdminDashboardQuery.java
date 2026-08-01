package top.blogapi.admin.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.admin.domain.repository.AdminStatsRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAdminDashboardQuery {

    private final AdminStatsRepository adminStatsRepository;

    public Map<String, Long> execute() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("users", adminStatsRepository.countUsers());
        stats.put("blogs", adminStatsRepository.countPublishedBlogs());
        stats.put("comments", adminStatsRepository.countComments());
        stats.put("views", adminStatsRepository.totalViews());
        return stats;
    }
}
