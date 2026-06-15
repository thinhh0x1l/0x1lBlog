package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.Follow;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface FollowRepository {

    @Insert("INSERT INTO follow (follower_id, following_id, created_at) VALUES (#{followerId}, #{followingId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Follow follow);

    @Delete("DELETE FROM follow WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    int delete(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Select("SELECT * FROM follow WHERE follower_id = #{followerId} AND following_id = #{followingId} LIMIT 1")
    Optional<Follow> findByFollowerAndFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Select("SELECT COUNT(*) > 0 FROM follow WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    boolean exists(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Select("SELECT f.*, u.nickname, u.avatar FROM follow f JOIN \"user\" u ON f.following_id = u.id WHERE f.follower_id = #{followerId}")
    List<Follow> findFollowingByFollowerId(@Param("followerId") Long followerId);

    @Select("SELECT COUNT(*) FROM follow WHERE following_id = #{userId}")
    int countFollowers(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM follow WHERE follower_id = #{userId}")
    int countFollowing(@Param("userId") Long userId);
}
