package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.blind.BlindChallenge;
import top.blogapi.model.entity.blind.BlindChallengeGuess;
import top.blogapi.service.blind.BlindChallengeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Orchestrates blind challenge operations: daily challenge retrieval, guessing, leaderboard, and bonuses.
 */
@Component
@RequiredArgsConstructor
public class BlindChallengeOrchestrator {

    private final BlindChallengeService blindChallengeService;

    public BlindChallenge getTodayChallenge() {
        return blindChallengeService.getTodayChallenge();
    }

    public BlindChallenge getChallengeStatus(LocalDate date) {
        return blindChallengeService.getChallengeStatus(date);
    }

    @Transactional
    public BlindChallengeGuess makeGuess(Long userId, Long guessedTopicId) {
        return blindChallengeService.makeGuess(userId, guessedTopicId);
    }

    @Transactional
    public BlindChallenge revealTopic() {
        return blindChallengeService.revealTopic();
    }

    public List<Map<String, Object>> getLeaderboard(LocalDate date) {
        return blindChallengeService.getLeaderboard(date);
    }

    public BlindChallengeGuess getCurrentGuess(Long userId) {
        return blindChallengeService.getCurrentGuess(userId);
    }

    @Transactional
    public int checkAndAwardBonuses() {
        return blindChallengeService.checkAndAwardBonuses();
    }
}
