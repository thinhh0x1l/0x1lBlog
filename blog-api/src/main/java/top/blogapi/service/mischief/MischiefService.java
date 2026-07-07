package top.blogapi.service.mischief;

import top.blogapi.model.entity.Badge;
import top.blogapi.model.entity.UserBadge;

import java.util.List;
import java.util.Map;

/**
 * Giao diện service cho hệ thống trò nghịch và huy hiệu, xử lý trao huy hiệu,
 * truy vấn huy hiệu người dùng và kiểm tra tự động dựa trên tiêu chí.
 */
public interface MischiefService {
    List<Badge> getAllBadges();
    void awardBadge(Long userId, Long badgeId, Long awardedBy);
    List<Map<String, Object>> getUserBadges(Long userId);
    List<Badge> checkAndAward(Long userId);
}
