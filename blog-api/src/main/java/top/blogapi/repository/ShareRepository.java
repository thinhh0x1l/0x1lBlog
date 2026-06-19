package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Share;

import java.util.List;

@Mapper
public interface ShareRepository {

    @Insert("""
        INSERT INTO shares (blog_id, user_id, quote_text, platform)
        VALUES (#{blogId}, #{userId}, #{quoteText}, #{platform})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Share share);

    @Select("SELECT * FROM shares WHERE blog_id = #{blogId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Share> findByBlogId(@Param("blogId") Long blogId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM shares WHERE blog_id = #{blogId}")
    long countByBlogId(Long blogId);
}
