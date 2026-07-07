package top.blogapi.service.dashboard;

import java.util.Map;

/**
 * Giao diện service cho bảng điều khiển admin, tổng hợp thống kê
 * toàn trang như số lượng người dùng, blog, bình luận và lượt xem.
 */
public interface DashboardService {
    long getTotalUsers();
    long getTotalBlogs();
    long getTotalComments();
    long getTotalViews();
    Map<String, Long> getStats();
}
