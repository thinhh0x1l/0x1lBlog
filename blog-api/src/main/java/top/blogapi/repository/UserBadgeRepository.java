package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.UserBadge;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code user_badges}. Liên kết người dùng với
 * huy hiệu đã đạt được và ngăn thưởng trùng lặp.
 */
@Mapper
public interface UserBadgeRepository {

    @Select("SELECT * FROM user_badges WHERE user_id = #{userId} ORDER BY awarded_at DESC")
    List<UserBadge> findByUserId(Long userId);

    @Insert("INSERT INTO user_badges (user_id, badge_id, awarded_by) VALUES (#{userId}, #{badgeId}, #{awardedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserBadge userBadge);

    @Select("SELECT EXISTS(SELECT 1 FROM user_badges WHERE user_id = #{userId} AND badge_id = #{badgeId})")
    boolean exists(Long userId, Long badgeId);
}
