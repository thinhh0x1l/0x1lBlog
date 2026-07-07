package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Follow;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code follows}. Xử lý theo dõi/hủy theo dõi,
 * truy vấn phân trang người theo dõi/đang theo dõi và kiểm tra tồn tại.
 */
@Mapper
public interface FollowRepository {

    @Select("SELECT * FROM follows WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    Optional<Follow> findByPair(Long followerId, Long followingId);

    @Insert("INSERT INTO follows (follower_id, following_id) VALUES (#{followerId}, #{followingId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Follow follow);

    @Delete("DELETE FROM follows WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    int delete(Long followerId, Long followingId);

    @Select("SELECT * FROM follows WHERE follower_id = #{followerId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Follow> findByFollowerId(@Param("followerId") Long followerId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM follows WHERE following_id = #{followingId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Follow> findByFollowingId(@Param("followingId") Long followingId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM follows WHERE follower_id = #{followerId}")
    long countByFollowerId(Long followerId);

    @Select("SELECT COUNT(*) FROM follows WHERE following_id = #{followingId}")
    long countByFollowingId(Long followingId);

    @Select("SELECT EXISTS(SELECT 1 FROM follows WHERE follower_id = #{followerId} AND following_id = #{followingId})")
    boolean exists(Long followerId, Long followingId);
}
