package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.service.dashboard.DashboardService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DashboardOrchestrator {

    private final DashboardService dashboardService;

    public Map<String, Long> getStats() {
        return dashboardService.getStats();
    }
}
