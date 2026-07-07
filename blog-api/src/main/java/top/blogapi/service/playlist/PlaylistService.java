package top.blogapi.service.playlist;

import top.blogapi.model.entity.Playlist;
import top.blogapi.model.entity.PlaylistSong;
import top.blogapi.model.entity.PlaylistVote;

import java.util.List;
import java.util.Optional;

/**
 * Giao diện service quản lý danh sách phát, hỗ trợ thêm, xóa,
 * sắp xếp bài hát và bỏ phiếu cộng đồng với tự động xóa khi điểm thấp.
 */
public interface PlaylistService {

    Playlist findOrCreateByOwnerId(Long ownerId);

    Playlist findById(Long id);

    Playlist findByOwnerId(Long ownerId);

    PlaylistSong findSongById(Long songId);

    PlaylistSong addSong(PlaylistSong song);

    void removeSong(Long songId);

    void reorderSongs(Long playlistId, List<Long> songIds);

    PlaylistSong voteSong(Long songId, Long userId, int vote);

    List<PlaylistSong> getSongsByPlaylistId(Long playlistId);

    int countSongsByPlaylistId(Long playlistId);

    int countTodaySongsByUserAndPlaylist(Long playlistId, Long userId);

    Optional<PlaylistSong> findSongByPlaylistAndSource(Long playlistId, String source, String sourceId);
}
