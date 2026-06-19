package top.blogapi.service.dashboard;

import java.util.Map;

public interface DashboardService {
    long getTotalUsers();
    long getTotalBlogs();
    long getTotalComments();
    long getTotalViews();
    Map<String, Long> getStats();
}
