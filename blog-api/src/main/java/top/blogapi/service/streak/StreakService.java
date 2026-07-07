package top.blogapi.service.streak;

/**
 * Giao diện service cho chuỗi điểm danh hàng ngày, theo dõi số ngày điểm danh
 * liên tiếp và thưởng điểm kinh nghiệm dựa trên độ dài chuỗi.
 */
public interface StreakService {
    int checkin(Long userId);
    int getStreak(Long userId);
}
