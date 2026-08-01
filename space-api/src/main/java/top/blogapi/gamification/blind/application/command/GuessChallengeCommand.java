package top.blogapi.gamification.blind.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.blind.domain.entity.BlindChallenge;
import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;
import top.blogapi.gamification.blind.domain.service.BlindChallengeService;

@Service
@RequiredArgsConstructor
public class GuessChallengeCommand {

    private final BlindChallengeService blindChallengeService;

    @Transactional
    public BlindChallengeGuess makeGuess(Long userId, Long guessedTopicId) {
        return blindChallengeService.makeGuess(userId, guessedTopicId);
    }

    @Transactional
    public BlindChallenge revealTopic() {
        return blindChallengeService.revealTopic();
    }

    @Transactional
    public int checkAndAwardBonuses() {
        return blindChallengeService.checkAndAwardBonuses();
    }
}
