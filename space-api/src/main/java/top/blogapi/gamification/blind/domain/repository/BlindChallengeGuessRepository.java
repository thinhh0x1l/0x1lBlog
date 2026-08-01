package top.blogapi.gamification.blind.domain.repository;

import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;

import java.util.List;
import java.util.Optional;

public interface BlindChallengeGuessRepository {

    Optional<BlindChallengeGuess> findByChallengeIdAndUserId(Long challengeId, Long userId);

    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);

    List<BlindChallengeGuess> findByChallengeIdAndIsCorrectTrue(Long challengeId);

    void save(BlindChallengeGuess guess);

    void markCorrect(Long challengeId, Long userId, Boolean isCorrect);

    List<BlindChallengeGuess> findLeaderboard(Long challengeId);

    List<BlindChallengeGuess> findByChallengeId(Long challengeId);
}
