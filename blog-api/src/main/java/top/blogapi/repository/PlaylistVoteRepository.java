package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.PlaylistVote;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code playlist_votes}. Theo dõi bình chọn
 * tăng/giảm trên bài hát với tổng hợp phiếu theo bài.
 */
@Mapper
public interface PlaylistVoteRepository {

    @Select("SELECT * FROM playlist_votes WHERE song_id = #{songId} AND user_id = #{userId}")
    Optional<PlaylistVote> findBySongAndUser(@Param("songId") Long songId, @Param("userId") Long userId);

    @Select("SELECT * FROM playlist_votes WHERE song_id = #{songId}")
    List<PlaylistVote> findBySongId(Long songId);

    @Insert("""
        INSERT INTO playlist_votes (playlist_id, song_id, user_id, vote, created_at)
        VALUES (#{playlistId}, #{songId}, #{userId}, #{vote}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PlaylistVote vote);

    @Update("UPDATE playlist_votes SET vote = #{vote} WHERE id = #{id}")
    int updateVote(PlaylistVote vote);

    @Delete("DELETE FROM playlist_votes WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COALESCE(SUM(vote), 0) FROM playlist_votes WHERE song_id = #{songId}")
    int sumVotesBySongId(Long songId);
}
