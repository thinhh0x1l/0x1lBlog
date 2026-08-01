package top.blogapi.user.auth.infrastructure.repository;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SessionMybatisMapper {

    @Update("""
        UPDATE sessions SET ended_at = NOW(), duration_seconds = EXTRACT(EPOCH FROM (NOW() - started_at))::INT
        WHERE id = #{id}
    """)
    void endSession(@Param("id") Long id);

    @Update("UPDATE sessions SET user_id = #{userId} WHERE id = #{id}")
    void setUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Delete("DELETE FROM sessions WHERE created_at < NOW() - INTERVAL '90 days'")
    void deleteOlderThan90Days();
}
