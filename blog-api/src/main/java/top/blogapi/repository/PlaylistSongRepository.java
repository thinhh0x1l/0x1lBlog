package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.PlaylistSong;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code playlist_songs}. Quản lý bài hát trong
 * playlist bao gồm sắp xếp, theo dõi bình chọn và giới hạn thêm hàng ngày.
 */
@Mapper
public interface PlaylistSongRepository {

    @Select("SELECT * FROM playlist_songs WHERE id = #{id}")
    Optional<PlaylistSong> findById(Long id);

    @Select("SELECT * FROM playlist_songs WHERE playlist_id = #{playlistId} ORDER BY sort_order ASC")
    List<PlaylistSong> findByPlaylistIdOrderBySortOrder(Long playlistId);

    @Select("""
        SELECT * FROM playlist_songs
        WHERE playlist_id = #{playlistId} AND source = #{source} AND source_id = #{sourceId}
    """)
    Optional<PlaylistSong> findByPlaylistAndSource(
            @Param("playlistId") Long playlistId,
            @Param("source") String source,
            @Param("sourceId") String sourceId);

    @Select("SELECT COUNT(*) FROM playlist_songs WHERE playlist_id = #{playlistId}")
    int countByPlaylistId(Long playlistId);

    @Select("""
        SELECT COUNT(*) FROM playlist_songs
        WHERE playlist_id = #{playlistId} AND added_by = #{addedBy}
        AND created_at >= CURRENT_DATE
    """)
    int countTodayByPlaylistAndUser(@Param("playlistId") Long playlistId, @Param("addedBy") Long addedBy);

    @Insert("""
        INSERT INTO playlist_songs (playlist_id, added_by, title, artist, source, source_id,
            thumbnail_url, duration_sec, sort_order, vote_count, created_at)
        VALUES (#{playlistId}, #{addedBy}, #{title}, #{artist}, #{source}, #{sourceId},
            #{thumbnailUrl}, #{durationSec}, #{sortOrder}, #{voteCount}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PlaylistSong song);

    @Update("UPDATE playlist_songs SET sort_order = #{sortOrder} WHERE id = #{id}")
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);

    @Update("UPDATE playlist_songs SET vote_count = #{voteCount} WHERE id = #{id}")
    int updateVoteCount(@Param("id") Long id, @Param("voteCount") int voteCount);

    @Delete("DELETE FROM playlist_songs WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COALESCE(MAX(sort_order), -1) + 1 FROM playlist_songs WHERE playlist_id = #{playlistId}")
    int nextSortOrder(Long playlistId);
}
