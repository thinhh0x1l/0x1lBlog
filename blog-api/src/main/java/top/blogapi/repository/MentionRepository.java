package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Mention;

import java.util.List;

/**
 * MyBatis mapper cho bảng {@code mentions}. Ghi lại và truy xuất đề cập
 * người dùng trên nhiều nguồn nội dung.
 */
@Mapper
public interface MentionRepository {

    @Insert("""
        INSERT INTO mentions (target_user_id, mentioned_by, source_type, source_id)
        VALUES (#{targetUserId}, #{mentionedBy}, #{sourceType}, #{sourceId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Mention mention);

    @Select("SELECT * FROM mentions WHERE target_user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Mention> findByTargetUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);
}
