package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.Playlist;

import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code playlists}. Quản lý siêu dữ liệu playlist
 * và thao tác đếm bài hát.
 */
@Mapper
public interface PlaylistRepository {

    @Select("SELECT * FROM playlists WHERE id = #{id}")
    Optional<Playlist> findById(Long id);

    @Select("SELECT * FROM playlists WHERE owner_id = #{ownerId}")
    Optional<Playlist> findByOwnerId(Long ownerId);

    @Insert("""
        INSERT INTO playlists (owner_id, title, is_public, song_count, created_at, updated_at)
        VALUES (#{ownerId}, #{title}, #{isPublic}, #{songCount}, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Playlist playlist);

    @Update("UPDATE playlists SET title = #{title}, is_public = #{isPublic}, song_count = #{songCount}, updated_at = NOW() WHERE id = #{id}")
    int update(Playlist playlist);

    @Update("UPDATE playlists SET song_count = song_count + 1, updated_at = NOW() WHERE id = #{id}")
    int incrementSongCount(Long id);

    @Update("UPDATE playlists SET song_count = GREATEST(song_count - 1, 0), updated_at = NOW() WHERE id = #{id}")
    int decrementSongCount(Long id);
}
