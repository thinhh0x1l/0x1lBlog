package top.blogapi.social.playlist.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistSongJpaRepository extends JpaRepository<PlaylistSong, Long> {

    List<PlaylistSong> findByPlaylistIdOrderBySortOrderAsc(Long playlistId);

    Optional<PlaylistSong> findByPlaylistIdAndSourceAndSourceId(Long playlistId, String source, String sourceId);

    @Query(value = "SELECT COUNT(*) FROM playlist_songs WHERE playlist_id = :playlistId", nativeQuery = true)
    int countByPlaylistId(@Param("playlistId") Long playlistId);

    @Modifying
    @Query(value = "UPDATE playlist_songs SET sort_order = :sortOrder WHERE id = :id", nativeQuery = true)
    void updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);

    @Modifying
    @Query(value = "UPDATE playlist_songs SET vote_count = :voteCount WHERE id = :id", nativeQuery = true)
    void updateVoteCount(@Param("id") Long id, @Param("voteCount") int voteCount);
}
