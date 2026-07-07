package top.blogapi.service.blind;

import top.blogapi.model.entity.blind.BlindChallenge;
import top.blogapi.model.entity.blind.BlindChallengeGuess;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Giao diện service cho trò chơi đoán chủ đề ẩn hàng ngày,
 * nơi người dùng đoán chủ đề và cạnh tranh trên bảng xếp hạng.
 */
public interface BlindChallengeService {
    BlindChallenge getTodayChallenge();
    BlindChallenge getChallengeStatus(LocalDate date);
    BlindChallengeGuess makeGuess(Long userId, Long guessedTopicId);
    BlindChallenge revealTopic();
    List<Map<String, Object>> getLeaderboard(LocalDate date);
    BlindChallengeGuess getCurrentGuess(Long userId);
    int checkAndAwardBonuses();
}
