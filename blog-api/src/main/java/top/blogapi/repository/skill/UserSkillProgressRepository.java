package top.blogapi.repository.skill;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.skill.UserSkillProgress;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code user_skill_progress}. Theo dõi điểm kỹ
 * năng của người dùng tích lũy trong mỗi danh mục.
 */
@Mapper
public interface UserSkillProgressRepository {

    @Select("SELECT * FROM user_skill_progress WHERE user_id = #{userId} AND category_id = #{categoryId}")
    Optional<UserSkillProgress> findByUserIdAndCategoryId(Long userId, Long categoryId);

    @Select("SELECT * FROM user_skill_progress WHERE user_id = #{userId}")
    List<UserSkillProgress> findByUserId(Long userId);

    @Insert("""
        INSERT INTO user_skill_progress (user_id, category_id, total_points)
        VALUES (#{userId}, #{categoryId}, #{points})
        ON CONFLICT (user_id, category_id)
        DO UPDATE SET total_points = user_skill_progress.total_points + #{points}
    """)
    int addPoints(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("points") int points);

    @Delete("DELETE FROM user_skill_progress WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);
}
