package top.blogapi.social.playlist.domain.repository;

import top.blogapi.social.playlist.domain.entity.PlaylistSong;

import java.util.List;
import java.util.Optional;

public interface PlaylistSongRepository {

    Optional<PlaylistSong> findById(Long id);

    List<PlaylistSong> findByPlaylistIdOrderBySortOrder(Long playlistId);

    Optional<PlaylistSong> findByPlaylistAndSource(Long playlistId, String source, String sourceId);

    int countByPlaylistId(Long playlistId);

    int countTodayByPlaylistAndUser(Long playlistId, Long addedBy);

    void insert(PlaylistSong song);

    void updateSortOrder(Long id, Integer sortOrder);

    void updateVoteCount(Long id, int voteCount);

    void deleteById(Long id);

    int nextSortOrder(Long playlistId);
}
