package top.blogapi.gamification.skill.infrastructure.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserSkillProgressMybatisMapper {

    @Update("""
        INSERT INTO user_skill_progress (user_id, category_id, total_points)
        VALUES (#{userId}, #{categoryId}, #{points})
        ON CONFLICT (user_id, category_id)
        DO UPDATE SET total_points = user_skill_progress.total_points + #{points}
    """)
    void addPoints(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("points") int points);
}
