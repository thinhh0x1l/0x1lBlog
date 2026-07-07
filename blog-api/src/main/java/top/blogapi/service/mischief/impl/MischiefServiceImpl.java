package top.blogapi.service.mischief.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Badge;
import top.blogapi.model.entity.User;
import top.blogapi.model.entity.UserBadge;
import top.blogapi.repository.BadgeRepository;
import top.blogapi.repository.UserBadgeRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.badge.BadgeService;
import top.blogapi.service.mischief.MischiefService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai MischiefService xử lý trao huy hiệu với chống trùng lặp
 * và kiểm tra tự động dựa trên tiêu chí người dùng (streak, cấp, exp).
 */
public class MischiefServiceImpl implements MischiefService {

    private final BadgeService badgeService;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    @Override
    public List<Badge> getAllBadges() {
        return badgeService.findAll();
    }

    @Override
    public void awardBadge(Long userId, Long badgeId, Long awardedBy) {
        badgeRepository.findById(badgeId)
                .orElseThrow(() -> new AppException(ErrorCode.BADGE_NOT_FOUND));

        if (userBadgeRepository.exists(userId, badgeId)) {
            throw new AppException(ErrorCode.BADGE_ALREADY_AWARDED);
        }

        badgeService.awardBadge(userId, badgeId, awardedBy);
    }

    @Override
    public List<Map<String, Object>> getUserBadges(Long userId) {
        List<UserBadge> userBadges = userBadgeRepository.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserBadge ub : userBadges) {
            Badge badge = badgeRepository.findById(ub.getBadgeId()).orElse(null);
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", ub.getId());
            entry.put("badgeId", ub.getBadgeId());
            entry.put("awardedBy", ub.getAwardedBy());
            entry.put("awardedAt", ub.getAwardedAt());
            if (badge != null) {
                entry.put("name", badge.getName());
                entry.put("displayName", badge.getDisplayName());
                entry.put("description", badge.getDescription());
                entry.put("iconUrl", badge.getIconUrl());
                entry.put("tier", badge.getTier());
            }
            result.add(entry);
        }
        return result;
    }

    @Override
    public List<Badge> checkAndAward(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Badge> allBadges = badgeRepository.findAll();
        List<Badge> awarded = new ArrayList<>();

        for (Badge badge : allBadges) {
            if (userBadgeRepository.exists(userId, badge.getId())) {
                continue;
            }
            if (meetsCriteria(user, badge)) {
                badgeService.awardBadge(userId, badge.getId(), null);
                awarded.add(badge);
            }
        }

        return awarded;
    }

    private boolean meetsCriteria(User user, Badge badge) {
        if (badge.getCriteria() == null || badge.getCriteria().isBlank()) {
            return false;
        }
        try {
            String criteria = badge.getCriteria().toLowerCase();
            if (criteria.contains("streak") && user.getCheckinStreak() != null) {
                int streak = user.getCheckinStreak();
                if (criteria.contains("\"streak\"")) {
                    int required = extractInt(criteria, "streak");
                    if (streak >= required) return true;
                }
            }
            if (criteria.contains("level") && user.getLevel() != null) {
                int level = user.getLevel();
                int required = extractInt(criteria, "level");
                if (level >= required) return true;
            }
            if (criteria.contains("exp") && user.getExp() != null) {
                long exp = user.getExp();
                int required = extractInt(criteria, "exp");
                if (exp >= required) return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private int extractInt(String json, String key) {
        int idx = json.indexOf("\"" + key + "\"");
        if (idx == -1) return Integer.MAX_VALUE;
        int colon = json.indexOf(":", idx + key.length() + 2);
        if (colon == -1) return Integer.MAX_VALUE;
        StringBuilder num = new StringBuilder();
        for (int i = colon + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c)) {
                num.append(c);
            } else if (num.length() > 0) {
                break;
            }
        }
        return num.length() > 0 ? Integer.parseInt(num.toString()) : Integer.MAX_VALUE;
    }
}
