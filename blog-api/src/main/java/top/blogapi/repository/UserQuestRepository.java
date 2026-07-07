package top.blogapi.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.blogapi.model.entity.UserQuest;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code user_quests}. Theo dõi tiến trình nhiệm
 * vụ của người dùng, lọc nhiệm vụ đang hoạt động và hoàn thành nhiệm vụ.
 */
@Mapper
public interface UserQuestRepository {

    @Select("SELECT * FROM user_quests WHERE user_id = #{userId}")
    List<UserQuest> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_quests WHERE user_id = #{userId} AND status = #{status}")
    List<UserQuest> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    @Select("SELECT * FROM user_quests WHERE user_id = #{userId} AND quest_id = #{questId} AND status = 'IN_PROGRESS'")
    Optional<UserQuest> findActiveByUserIdAndQuestId(@Param("userId") Long userId, @Param("questId") Long questId);

    @Select("SELECT * FROM user_quests WHERE user_id = #{userId} AND status = 'IN_PROGRESS' AND expires_at > NOW()")
    List<UserQuest> findActiveQuests(@Param("userId") Long userId);

    @Select("SELECT * FROM user_quests WHERE id = #{id}")
    Optional<UserQuest> findById(@Param("id") Long id);

    @Insert("INSERT INTO user_quests (user_id, quest_id, progress, target, status, expires_at) VALUES (#{userId}, #{questId}, 0, #{target}, 'IN_PROGRESS', #{expiresAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserQuest userQuest);

    @Update("UPDATE user_quests SET progress = #{progress}, status = #{status}, claimed_at = #{claimedAt} WHERE id = #{id}")
    void update(UserQuest userQuest);

    @Select("SELECT COUNT(*) FROM user_quests WHERE user_id = #{userId} AND status = 'IN_PROGRESS' AND quest_id IN (SELECT id FROM quests WHERE type = #{type})")
    int countActiveByType(@Param("userId") Long userId, @Param("type") String type);
}
