package top.blogapi.social.playlist.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.playlist.domain.entity.Playlist;

import java.util.Optional;

@Repository
public interface PlaylistJpaRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findFirstByOwnerId(Long ownerId);

    @Modifying
    @Query(value = "UPDATE playlists SET song_count = song_count + 1, updated_at = NOW() WHERE id = :id", nativeQuery = true)
    void incrementSongCount(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE playlists SET song_count = GREATEST(song_count - 1, 0), updated_at = NOW() WHERE id = :id", nativeQuery = true)
    void decrementSongCount(@Param("id") Long id);
}
