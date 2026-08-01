package top.blogapi.social.playlist.domain.repository;

import top.blogapi.social.playlist.domain.entity.Playlist;

import java.util.Optional;

public interface PlaylistRepository {

    Optional<Playlist> findById(Long id);

    Optional<Playlist> findByOwnerId(Long ownerId);

    void insert(Playlist playlist);

    void update(Playlist playlist);

    void incrementSongCount(Long id);

    void decrementSongCount(Long id);
}
