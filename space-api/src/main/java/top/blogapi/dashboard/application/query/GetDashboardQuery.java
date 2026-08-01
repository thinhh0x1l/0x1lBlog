package top.blogapi.dashboard.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.dashboard.domain.service.DashboardService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetDashboardQuery {

    private final DashboardService dashboardService;

    public Map<String, Long> execute() {
        return dashboardService.getStats();
    }
}
