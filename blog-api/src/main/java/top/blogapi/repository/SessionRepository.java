package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Session;

import java.util.Optional;

@Mapper
public interface SessionRepository {

    @Select("SELECT * FROM sessions WHERE id = #{id}")
    Optional<Session> findById(Long id);

    @Select("SELECT * FROM sessions WHERE session_id = #{sessionId}")
    Optional<Session> findBySessionId(String sessionId);

    @Insert("""
        INSERT INTO sessions (session_id, user_id, ip_address, user_agent, device_type, country_code, city)
        VALUES (#{sessionId}, #{userId}, #{ipAddress}::inet, #{userAgent}, #{deviceType}, #{countryCode}, #{city})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Session session);

    @Update("""
        UPDATE sessions SET ended_at = NOW(), duration_seconds = EXTRACT(EPOCH FROM (NOW() - started_at))::INT
        WHERE id = #{id}
    """)
    int endSession(Long id);

    @Update("""
        UPDATE sessions SET user_id = #{userId} WHERE id = #{id}
    """)
    int setUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Delete("DELETE FROM sessions WHERE created_at < NOW() - INTERVAL '90 days'")
    int deleteOlderThan90Days();
}
