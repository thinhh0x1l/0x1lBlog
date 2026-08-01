package top.blogapi.social.playlist.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.repository.PlaylistRepository;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final PlaylistJpaRepository jpa;

    @Override
    public Optional<Playlist> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Playlist> findByOwnerId(Long ownerId) {
        return jpa.findFirstByOwnerId(ownerId);
    }

    @Override
    public void insert(Playlist playlist) {
        Instant now = Instant.now();
        if (playlist.getCreatedAt() == null) {
            playlist.setCreatedAt(now);
        }
        if (playlist.getUpdatedAt() == null) {
            playlist.setUpdatedAt(now);
        }
        jpa.save(playlist);
    }

    @Override
    public void update(Playlist playlist) {
        playlist.setUpdatedAt(Instant.now());
        jpa.save(playlist);
    }

    @Override
    public void incrementSongCount(Long id) {
        jpa.incrementSongCount(id);
    }

    @Override
    public void decrementSongCount(Long id) {
        jpa.decrementSongCount(id);
    }
}
