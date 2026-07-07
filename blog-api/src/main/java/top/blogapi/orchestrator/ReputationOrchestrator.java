package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.service.reputation.ReputationService;

import java.util.Map;

/**
 * Điều phối truy xuất điểm uy tín của người dùng.
 */
@Component
@RequiredArgsConstructor
public class ReputationOrchestrator {

    private final ReputationService reputationService;

    public Map<String, Long> getScores(Long userId) {
        return reputationService.getScores(userId);
    }
}
