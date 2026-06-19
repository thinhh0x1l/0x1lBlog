package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.BlogHashtag;

@Mapper
public interface BlogHashtagRepository {

    @Insert("INSERT INTO blog_hashtags (blog_id, hashtag_id) VALUES (#{blogId}, #{hashtagId}) ON CONFLICT DO NOTHING")
    int insert(BlogHashtag blogHashtag);

    @Delete("DELETE FROM blog_hashtags WHERE blog_id = #{blogId} AND hashtag_id = #{hashtagId}")
    int delete(Long blogId, Long hashtagId);

    @Delete("DELETE FROM blog_hashtags WHERE blog_id = #{blogId}")
    int deleteByBlogId(Long blogId);
}
