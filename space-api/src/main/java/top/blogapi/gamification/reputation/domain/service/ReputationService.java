package top.blogapi.gamification.reputation.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.user.core.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReputationService {

    private static final String COL_WRITING = "reputation_writing";
    private static final String COL_COMMUNITY = "reputation_community";
    private static final String COL_CREATIVITY = "reputation_creativity";
    private static final String COL_INFLUENCE = "reputation_influence";

    private final UserRepository userRepository;

    public void addWriting(Long userId, long amount) {
        userRepository.addReputation(userId, COL_WRITING, amount);
    }

    public void addCommunity(Long userId, long amount) {
        userRepository.addReputation(userId, COL_COMMUNITY, amount);
    }

    public void addCreativity(Long userId, long amount) {
        userRepository.addReputation(userId, COL_CREATIVITY, amount);
    }

    public void addInfluence(Long userId, long amount) {
        userRepository.addReputation(userId, COL_INFLUENCE, amount);
    }

    public Map<String, Long> getScores(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Long> scores = new HashMap<>();
        scores.put("writing", user.getReputationWriting() != null ? user.getReputationWriting() : 0L);
        scores.put("community", user.getReputationCommunity() != null ? user.getReputationCommunity() : 0L);
        scores.put("creativity", user.getReputationCreativity() != null ? user.getReputationCreativity() : 0L);
        scores.put("influence", user.getReputationInfluence() != null ? user.getReputationInfluence() : 0L);
        return scores;
    }
}
