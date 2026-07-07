package top.blogapi.service.badge;

import top.blogapi.model.entity.Badge;

import java.util.List;

/**
 * Giao diện service quản lý huy hiệu, bao gồm thao tác CRUD và trao thưởng huy hiệu cho người dùng.
 */
public interface BadgeService {
    List<Badge> findAll();
    Badge create(Badge badge);
    void awardBadge(Long userId, Long badgeId, Long awardedBy);
    boolean hasBadge(Long userId, Long badgeId);
}
