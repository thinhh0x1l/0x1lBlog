package top.blogapi.repository.skill;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.skill.UserSkillUnlock;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code user_skill_unlocks}. Ghi lại kỹ năng
 * người dùng đã mở khóa và ngăn mở khóa trùng lặp.
 */
@Mapper
public interface UserSkillUnlockRepository {

    @Select("SELECT * FROM user_skill_unlocks WHERE user_id = #{userId} AND skill_id = #{skillId}")
    Optional<UserSkillUnlock> findByUserIdAndSkillId(Long userId, Long skillId);

    @Select("SELECT * FROM user_skill_unlocks WHERE user_id = #{userId}")
    List<UserSkillUnlock> findByUserId(Long userId);

    @Select("SELECT EXISTS(SELECT 1 FROM user_skill_unlocks WHERE user_id = #{userId} AND skill_id = #{skillId})")
    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    @Insert("INSERT INTO user_skill_unlocks (user_id, skill_id) VALUES (#{userId}, #{skillId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserSkillUnlock unlock);

    @Delete("DELETE FROM user_skill_unlocks WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);
}
