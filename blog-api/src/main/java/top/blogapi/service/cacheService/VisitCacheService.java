package top.blogapi.service.cacheService;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.blogapi.constant.HeaderConstant;
import top.blogapi.dto.internal.UserAgentDTO;
import top.blogapi.dto.internal.VisitDto;
import top.blogapi.model.entity.Visit;
import top.blogapi.service.VisitService;
import top.blogapi.service.impl.GeoIpService;
import top.blogapi.service.impl.orchestration.VisitOrchestrator;
import top.blogapi.util.IpAddressUtils;
import top.blogapi.util.UserAgentUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class VisitCacheService {
    VisitService visitService;
    GeoIpService geoIpService;
    UserAgentUtils userAgentUtils;

    private static final long flushIntervalMs = 30*60*1000;
    int cacheExpireSeconds = 30;
    long cacheMaxSize = 10000;
    int flushBatchSize = 1000;

    // Hàng đợi an toàn cho đa luồng đối với các lượt truy cập đã hết hạn
    Queue<VisitDto> visitExpires = new ConcurrentLinkedQueue<>();

    // Chỉ số
    AtomicLong cacheHits = new AtomicLong(0);
    AtomicLong cacheMisses = new AtomicLong(0);
    AtomicLong flushCount = new AtomicLong(0);
    AtomicLong totalVisits = new AtomicLong(0);

    Cache<Long, VisitDto> visitCache = buildVisitCache();


    public Visit getVisitOrCreate(Long guestId, HttpServletRequest request) {
        try {
            VisitDto visit = visitCache.getIfPresent(guestId);

            if (visit == null) {
                cacheMisses.incrementAndGet();
                visit = createVisit(request, guestId);
                visitCache.put(guestId, visit);
                totalVisits.incrementAndGet();
                log.debug("Created new visit for guest: {}", guestId);
            } else {
                cacheHits.incrementAndGet();
            }

            visit.setLastActivity(LocalDateTime.now());
            visit.getPv().incrementAndGet();

            return visit.getVisit();

        } catch (Exception e) {
            log.error("Error processing visit for guest: {}", guestId, e);
            // Fallback: create a basic visit without caching
            return createVisit(request, guestId).getVisit();
        }
    }

    public VisitDto createVisit(HttpServletRequest request, Long guestId) {
        try {
            String ip = IpAddressUtils.getIpAddress(request);
            String userAgent = request.getHeader(HeaderConstant.USER_AGENT);
            UserAgentDTO userAgentDTO = userAgentUtils.parseOsAndBrowser(userAgent);

            return VisitDto.builder()
                    .guestId(guestId)
                    .userAgent(userAgent)
                    .ip(ip)
                    .ipSource(geoIpService.getProvince(ip))
                    .pv(new AtomicLong(0))
                    .browser(userAgentDTO.getBrowser())
                    .os(userAgentDTO.getOs())
                    .startedAt(LocalDateTime.now())
                    .lastActivity(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Error creating visit for guest: {}", guestId, e);
            return VisitDto.builder()
                    .guestId(guestId)
                    .pv(new AtomicLong(0))
                    .startedAt(LocalDateTime.now())
                    .lastActivity(LocalDateTime.now())
                    .build();
        }
    }

    @PreDestroy
    public void flush() {
        log.info("Starting graceful shutdown flush. Cache size: {}, Queue size: {}",
                visitCache.estimatedSize(), visitExpires.size());

        try {
            Map<Long, VisitDto> snapshot = new HashMap<>(visitCache.asMap());
            List<Visit> allVisits = new ArrayList<>();

            VisitDto dto;
            while ((dto = visitExpires.poll()) != null) {
                allVisits.add(dto.getVisit());
            }

            snapshot.values().forEach(visitDto -> allVisits.add(visitDto.getVisit()));

            if (!allVisits.isEmpty()) {
                allVisits.sort(Comparator.comparing(Visit::getStartedAt));
                flushVisitsInBatches(allVisits);
                log.info("Shutdown flush completed. Processed {} visits", allVisits.size());
            }

            visitCache.invalidateAll();
            logMetrics();

        } catch (Exception e) {
            log.error("Error during shutdown flush", e);
        }
    }

    private void flushVisitsInBatches(List<Visit> visits) {
        for (int i = 0; i < visits.size(); i += flushBatchSize) {
            int endIndex = Math.min(i + flushBatchSize, visits.size());
            List<Visit> batch = visits.subList(i, endIndex);

            try {
                visitService.insertVisitFlush(batch);
            } catch (Exception e) {
                log.error("Failed to flush batch {}-{}", i, endIndex, e);
                // Continue with next batch
            }
        }
    }
    @Scheduled(fixedDelay = flushIntervalMs)
    public void flushFixed(){
        try {
            List<Visit> visitsToFlush = new ArrayList<>();

            VisitDto dto;
            int collected = 0;
            while ((dto = visitExpires.poll()) != null && collected < flushBatchSize) {
                visitsToFlush.add(dto.getVisit());
                collected++;
            }

            if (!visitsToFlush.isEmpty()) {
                visitService.insertVisitFlush(visitsToFlush);
                flushCount.incrementAndGet();
                log.debug("Flushed {} expired visits", visitsToFlush.size());
            }

            if (flushCount.get() % 5 == 0) {
                logMetrics();
            }

        } catch (Exception e) {
            log.error("Lỗi flush", e);
        }

    }
    public Map<String, Object> getHealthMetrics() {
        var stats = visitCache.stats();
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("cacheSize", visitCache.estimatedSize());
        metrics.put("queueSize", visitExpires.size());
        metrics.put("hitRate", stats.hitRate());
        metrics.put("totalHits", cacheHits.get());
        metrics.put("totalMisses", cacheMisses.get());
        metrics.put("flushCount", flushCount.get());
        metrics.put("isHealthy", isHealthy());
        return metrics;
    }

    public boolean isHealthy() {
        return visitExpires.size() < cacheMaxSize * 0.8; // Alert if queue is 80% full
    }

    private void logMetrics() {
        var stats = visitCache.stats();
        log.info("Cache Metrics - Hits: {}, Misses: {}, Hit Rate: {}%, " +
                        "Cache Size: {}, Queue Size: {}, Total Visits: {}, Flush Count: {}",
                cacheHits.get(), cacheMisses.get(),
                stats.hitRate() * 100,
                visitCache.estimatedSize(), visitExpires.size(),
                totalVisits.get(), flushCount.get());
    }


    private Cache<Long,VisitDto> buildVisitCache(){
        return Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterAccess(cacheExpireSeconds, TimeUnit.SECONDS)
                .recordStats()
                // có thể nói là: trigger removalListener sớm tránh lazy
                .scheduler(Scheduler.systemScheduler())
                .removalListener(
                        (Long guestId, VisitDto visit, RemovalCause cause) -> {
                    try {
                        if(cause == RemovalCause.EXPIRED){
                            if(!visitExpires.offer(visit)){
                                log.warn("Không thể thêm Expired visit vào Queue(isFull)");
                                // fallback: flush vào db
                                visitService.insertVisitFlush(List.of(visit.getVisit()));
                            }
                        }

                    } catch (Exception e) {
                        log.error("Xử lý lỗi khi xóa bộ nhớ cache cho Guest: {}", guestId, e);
                    }
                })
                .build();
    }


}
