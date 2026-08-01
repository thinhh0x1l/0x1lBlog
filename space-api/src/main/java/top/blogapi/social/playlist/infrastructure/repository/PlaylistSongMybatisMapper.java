package top.blogapi.social.playlist.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;

@Mapper
public interface PlaylistSongMybatisMapper {

    @Select("""
        SELECT COUNT(*) FROM playlist_songs
        WHERE playlist_id = #{playlistId} AND added_by = #{addedBy}
        AND created_at >= CURRENT_DATE
    """)
    int countTodayByPlaylistAndUser(@Param("playlistId") Long playlistId, @Param("addedBy") Long addedBy);

    @Select("SELECT COALESCE(MAX(sort_order), -1) + 1 FROM playlist_songs WHERE playlist_id = #{playlistId}")
    int nextSortOrder(@Param("playlistId") Long playlistId);
}
