package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.UserBadge;

import java.util.List;

@Repository
@Mapper
public interface UserBadgeRepository {

    @Insert("INSERT INTO user_badge (user_id, badge_id, awarded_at, awarded_by) VALUES (#{userId}, #{badgeId}, NOW(), #{awardedBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(UserBadge userBadge);

    @Delete("DELETE FROM user_badge WHERE user_id = #{userId} AND badge_id = #{badgeId}")
    int delete(@Param("userId") Long userId, @Param("badgeId") Long badgeId);

    @Select("SELECT ub.*, b.name, b.display_name, b.icon_url, b.tier FROM user_badge ub JOIN badge b ON ub.badge_id = b.id WHERE ub.user_id = #{userId}")
    List<UserBadge> findByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM user_badge WHERE badge_id = #{badgeId}")
    int countByBadgeId(@Param("badgeId") Long badgeId);
}
