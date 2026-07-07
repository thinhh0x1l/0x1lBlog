package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Reaction;

import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code reactions}. Hệ thống phản ứng chung hỗ
 * trợ upsert, chuyển đổi loại và truy vấn đếm trên mọi loại mục tiêu.
 */
@Mapper
public interface ReactionRepository {

    @Select("SELECT * FROM reactions WHERE user_id = #{userId} AND target_type = #{targetType} AND target_id = #{targetId}")
    Optional<Reaction> findByUserAndTarget(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Insert("""
        INSERT INTO reactions (user_id, target_type, target_id, type)
        VALUES (#{userId}, #{targetType}, #{targetId}, #{type})
        ON CONFLICT (user_id, target_type, target_id) DO UPDATE SET type = #{type}
    """)
    int upsert(Reaction reaction);

    @Delete("DELETE FROM reactions WHERE user_id = #{userId} AND target_type = #{targetType} AND target_id = #{targetId}")
    int delete(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);

    @Select("SELECT COUNT(*) FROM reactions WHERE target_type = #{targetType} AND target_id = #{targetId} AND type = #{type}")
    int countByTargetAndType(@Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("type") String type);

    @Select("SELECT type FROM reactions WHERE user_id = #{userId} AND target_type = #{targetType} AND target_id = #{targetId}")
    String findTypeByUserAndTarget(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("targetId") Long targetId);
}
