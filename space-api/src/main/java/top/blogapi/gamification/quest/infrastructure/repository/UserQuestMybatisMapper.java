package top.blogapi.gamification.quest.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserQuestMybatisMapper {

    @Select("SELECT COUNT(*) FROM user_quests WHERE user_id = #{userId} AND status = 'IN_PROGRESS' AND quest_id IN (SELECT id FROM quests WHERE type = #{type})")
    int countActiveByType(@Param("userId") Long userId, @Param("type") String type);
}
