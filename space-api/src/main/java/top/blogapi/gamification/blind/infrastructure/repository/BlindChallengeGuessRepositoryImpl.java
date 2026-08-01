package top.blogapi.gamification.blind.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;
import top.blogapi.gamification.blind.domain.repository.BlindChallengeGuessRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlindChallengeGuessRepositoryImpl implements BlindChallengeGuessRepository {

    private final BlindChallengeGuessJpaRepository jpa;
    private final BlindChallengeGuessMybatisMapper mybatis;

    @Override
    public Optional<BlindChallengeGuess> findByChallengeIdAndUserId(Long challengeId, Long userId) {
        return jpa.findByChallengeIdAndUserId(challengeId, userId);
    }

    @Override
    public boolean existsByChallengeIdAndUserId(Long challengeId, Long userId) {
        return jpa.existsByChallengeIdAndUserId(challengeId, userId);
    }

    @Override
    public List<BlindChallengeGuess> findByChallengeIdAndIsCorrectTrue(Long challengeId) {
        return jpa.findByChallengeIdAndIsCorrectTrue(challengeId);
    }

    @Override
    public void save(BlindChallengeGuess guess) {
        if (guess.getCreatedAt() == null) {
            guess.setCreatedAt(Instant.now());
        }
        jpa.save(guess);
    }

    @Override
    public void markCorrect(Long challengeId, Long userId, Boolean isCorrect) {
        jpa.markCorrect(challengeId, userId, isCorrect);
    }

    @Override
    public List<BlindChallengeGuess> findLeaderboard(Long challengeId) {
        return mybatis.findLeaderboard(challengeId);
    }

    @Override
    public List<BlindChallengeGuess> findByChallengeId(Long challengeId) {
        return jpa.findByChallengeId(challengeId);
    }
}
