package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.BlogHashtag;

import java.util.List;

/**
 * MyBatis mapper cho bảng liên kết {@code blog_hashtags}. Quản lý quan hệ
 * nhiều-nhiều giữa blog và hashtag.
 */
@Mapper
public interface BlogHashtagRepository {

    @Insert("INSERT INTO blog_hashtags (blog_id, hashtag_id) VALUES (#{blogId}, #{hashtagId}) ON CONFLICT DO NOTHING")
    int insert(BlogHashtag blogHashtag);

    @Insert({"<script>",
            "INSERT INTO blog_hashtags (blog_id, hashtag_id) VALUES ",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.blogId}, #{item.hashtagId})",
            "</foreach>",
            "ON CONFLICT DO NOTHING",
            "</script>"})
    int insertBatch(@Param("list") List<BlogHashtag> blogHashtags);

    @Delete("DELETE FROM blog_hashtags WHERE blog_id = #{blogId} AND hashtag_id = #{hashtagId}")
    int delete(Long blogId, Long hashtagId);

    @Delete("DELETE FROM blog_hashtags WHERE blog_id = #{blogId}")
    int deleteByBlogId(Long blogId);
}
