package top.blogapi.social.playlist.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;
import top.blogapi.social.playlist.domain.repository.PlaylistSongRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaylistSongRepositoryImpl implements PlaylistSongRepository {

    private final PlaylistSongJpaRepository jpa;
    private final PlaylistSongMybatisMapper mybatis;

    @Override
    public Optional<PlaylistSong> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<PlaylistSong> findByPlaylistIdOrderBySortOrder(Long playlistId) {
        return jpa.findByPlaylistIdOrderBySortOrderAsc(playlistId);
    }

    @Override
    public Optional<PlaylistSong> findByPlaylistAndSource(Long playlistId, String source, String sourceId) {
        return jpa.findByPlaylistIdAndSourceAndSourceId(playlistId, source, sourceId);
    }

    @Override
    public int countByPlaylistId(Long playlistId) {
        return jpa.countByPlaylistId(playlistId);
    }

    @Override
    public int countTodayByPlaylistAndUser(Long playlistId, Long addedBy) {
        return mybatis.countTodayByPlaylistAndUser(playlistId, addedBy);
    }

    @Override
    public void insert(PlaylistSong song) {
        if (song.getCreatedAt() == null) {
            song.setCreatedAt(Instant.now());
        }
        jpa.save(song);
    }

    @Override
    public void updateSortOrder(Long id, Integer sortOrder) {
        jpa.updateSortOrder(id, sortOrder);
    }

    @Override
    public void updateVoteCount(Long id, int voteCount) {
        jpa.updateVoteCount(id, voteCount);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public int nextSortOrder(Long playlistId) {
        return mybatis.nextSortOrder(playlistId);
    }
}
