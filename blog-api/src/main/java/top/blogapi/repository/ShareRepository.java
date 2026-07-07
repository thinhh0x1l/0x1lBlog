package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Share;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code shares}. Ghi lại lượt chia sẻ nội dung
 * và cung cấp truy vấn đếm theo mục tiêu.
 */
@Mapper
public interface ShareRepository {

    @Insert("""
        INSERT INTO shares (target_type, target_id, user_id, content)
        VALUES (#{targetType}, #{targetId}, #{userId}, #{content})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Share share);

    @Select("SELECT * FROM shares WHERE target_type = #{targetType} AND target_id = #{targetId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Share> findByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM shares WHERE target_type = #{targetType} AND target_id = #{targetId}")
    long countByTarget(@Param("targetType") String targetType, @Param("targetId") Long targetId);
}
