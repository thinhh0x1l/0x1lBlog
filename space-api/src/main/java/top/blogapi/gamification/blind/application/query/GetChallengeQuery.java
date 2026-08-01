package top.blogapi.gamification.blind.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.gamification.blind.domain.entity.BlindChallenge;
import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;
import top.blogapi.gamification.blind.domain.service.BlindChallengeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetChallengeQuery {

    private final BlindChallengeService blindChallengeService;

    public BlindChallenge getTodayChallenge() {
        return blindChallengeService.getTodayChallenge();
    }

    public BlindChallenge getChallengeStatus(LocalDate date) {
        return blindChallengeService.getChallengeStatus(date);
    }

    public List<Map<String, Object>> getLeaderboard(LocalDate date) {
        return blindChallengeService.getLeaderboard(date);
    }

    public BlindChallengeGuess getCurrentGuess(Long userId) {
        return blindChallengeService.getCurrentGuess(userId);
    }
}
