package top.blogapi.social.status.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.blogapi.social.status.domain.entity.Status;

import java.util.List;

@Mapper
public interface StatusMybatisMapper {

    @Select("SELECT * FROM statuses WHERE user_id = #{userId} AND deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Status> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM statuses WHERE thread_id = #{threadId} AND deleted_at IS NULL ORDER BY part_order ASC")
    List<Status> findThreadParts(@Param("threadId") Long threadId);

    @Select("SELECT * FROM statuses WHERE visibility IN ('PUBLIC', 'FOLLOWERS') AND deleted_at IS NULL ORDER BY created_at DESC LIMIT #{limit}")
    List<Status> findFeed(@Param("limit") int limit);

    @Update("UPDATE statuses SET content = #{content}, image_url = #{imageUrl}, updated_at = NOW() WHERE id = #{id} AND deleted_at IS NULL")
    void update(Status status);
}
