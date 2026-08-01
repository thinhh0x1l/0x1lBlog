package top.blogapi.social.story.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.blogapi.social.story.domain.entity.Story;

import java.util.List;

@Mapper
public interface StoryMybatisMapper {

    @Select("SELECT * FROM stories WHERE user_id = #{userId} AND deleted_at IS NULL AND expires_at > NOW() ORDER BY created_at DESC")
    List<Story> findActiveByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM stories WHERE deleted_at IS NULL AND expires_at > NOW() ORDER BY created_at DESC LIMIT #{limit}")
    List<Story> findActiveFeed(@Param("limit") int limit);
}
