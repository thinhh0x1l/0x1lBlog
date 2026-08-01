package top.blogapi.gamification.blind.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlindChallengeGuessJpaRepository extends JpaRepository<BlindChallengeGuess, Long> {

    Optional<BlindChallengeGuess> findByChallengeIdAndUserId(Long challengeId, Long userId);

    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);

    List<BlindChallengeGuess> findByChallengeIdAndIsCorrectTrue(Long challengeId);

    @Modifying
    @Query(value = "UPDATE blind_challenge_guesses SET is_correct = :isCorrect WHERE challenge_id = :challengeId AND user_id = :userId", nativeQuery = true)
    void markCorrect(@Param("challengeId") Long challengeId, @Param("userId") Long userId, @Param("isCorrect") Boolean isCorrect);

    List<BlindChallengeGuess> findByChallengeId(Long challengeId);
}
