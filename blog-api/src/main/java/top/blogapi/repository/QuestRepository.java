package top.blogapi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.blogapi.model.entity.Quest;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code quests}. Cung cấp truy vấn nhiệm vụ đang
 * hoạt động và lọc theo loại nhiệm vụ.
 */
@Mapper
public interface QuestRepository {

    @Select("SELECT * FROM quests WHERE is_active = TRUE")
    List<Quest> findActiveQuests();

    @Select("SELECT * FROM quests WHERE type = #{type} AND is_active = TRUE")
    List<Quest> findByType(@Param("type") String type);

    @Select("SELECT * FROM quests WHERE id = #{id}")
    Optional<Quest> findById(@Param("id") Long id);

    @Select("SELECT * FROM quests")
    List<Quest> findAll();
}
