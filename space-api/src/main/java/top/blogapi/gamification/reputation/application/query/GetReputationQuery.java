package top.blogapi.gamification.reputation.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.gamification.reputation.domain.service.ReputationService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetReputationQuery {

    private final ReputationService reputationService;

    public Map<String, Long> execute(Long userId) {
        return reputationService.getScores(userId);
    }
}
