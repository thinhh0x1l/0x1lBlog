package top.blogapi.service.sync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.blogapi.service.tracking.ViewTrackingService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final ViewTrackingService viewTrackingService;

    @Scheduled(fixedRate = 45000)
    public void syncViewCounters() {
        long flushed = viewTrackingService.flushViewsToDb();
        if (flushed > 0) {
            log.info("Flushed {} blog views to database", flushed);
        }
    }
}
