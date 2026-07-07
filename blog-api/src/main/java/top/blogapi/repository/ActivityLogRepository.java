package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.ActivityLog;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code activity_log}. Hỗ trợ thêm, truy vấn
 * theo trace/session/user/category và dọn dẹp log cũ theo lịch.
 */
@Mapper
public interface ActivityLogRepository {

    @Insert("""
        INSERT INTO activity_log (trace_id, session_id, user_id, category, action, source, target_type, target_id, metadata)
        VALUES (#{traceId}, #{sessionId}, #{userId}, #{category}, #{action}, #{source}, #{targetType}, #{targetId}, #{metadata}::jsonb)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ActivityLog log);

    @Select("SELECT * FROM activity_log WHERE trace_id = #{traceId} ORDER BY created_at")
    List<ActivityLog> findByTraceId(String traceId);

    @Select("SELECT * FROM activity_log WHERE session_id = #{sessionId} ORDER BY created_at DESC LIMIT #{limit}")
    List<ActivityLog> findBySessionId(@Param("sessionId") Long sessionId, @Param("limit") int limit);

    @Select("SELECT * FROM activity_log WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<ActivityLog> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM activity_log WHERE category = #{category} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<ActivityLog> findByCategory(@Param("category") String category, @Param("limit") int limit, @Param("offset") int offset);

    @Delete("DELETE FROM activity_log WHERE category = 'API' AND created_at < NOW() - INTERVAL '7 days'")
    int deleteApiOlderThan7Days();

    @Delete("DELETE FROM activity_log WHERE category = 'PAGE_VIEW' AND created_at < NOW() - INTERVAL '30 days'")
    int deletePageViewOlderThan30Days();

    @Delete("DELETE FROM activity_log WHERE category IN ('ACTION', 'SYSTEM') AND created_at < NOW() - INTERVAL '90 days'")
    int deleteActionSystemOlderThan90Days();
}
