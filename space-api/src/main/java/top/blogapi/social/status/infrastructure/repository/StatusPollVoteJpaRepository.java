package top.blogapi.social.status.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.social.status.domain.entity.StatusPollVote;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusPollVoteJpaRepository extends JpaRepository<StatusPollVote, Long> {

    List<StatusPollVote> findByPollId(Long pollId);

    Optional<StatusPollVote> findByPollIdAndUserId(Long pollId, Long userId);

    @Query(value = "SELECT COUNT(*) FROM status_poll_votes WHERE poll_id = :pollId AND option_index = :optionIndex", nativeQuery = true)
    long countByPollIdAndOptionIndex(@Param("pollId") Long pollId, @Param("optionIndex") Integer optionIndex);

    @Modifying
    @Query(value = "DELETE FROM status_poll_votes WHERE poll_id = :pollId AND user_id = :userId", nativeQuery = true)
    void deleteByPollIdAndUserId(@Param("pollId") Long pollId, @Param("userId") Long userId);
}
