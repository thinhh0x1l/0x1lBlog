package top.blogapi.social.playlist.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.playlist.domain.entity.PlaylistVote;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistVoteJpaRepository extends JpaRepository<PlaylistVote, Long> {

    Optional<PlaylistVote> findBySongIdAndUserId(Long songId, Long userId);

    List<PlaylistVote> findBySongId(Long songId);

    @Modifying
    @Query(value = "UPDATE playlist_votes SET vote = :vote WHERE id = :id", nativeQuery = true)
    void updateVote(@Param("id") Long id, @Param("vote") int vote);
}
