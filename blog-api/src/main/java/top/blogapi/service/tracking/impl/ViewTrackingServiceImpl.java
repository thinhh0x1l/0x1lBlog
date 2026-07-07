package top.blogapi.service.tracking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.blogapi.repository.BlogRepository;
import top.blogapi.service.tracking.ViewTrackingService;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewTrackingServiceImpl implements ViewTrackingService {

    private final StringRedisTemplate redisTemplate;
    private final BlogRepository blogRepository;

    private static final String VIEWS_TO_FLUSH_KEY = "views_to_flush";

    @Override
    public boolean isUniqueView(String sessionId, Long blogId) {
        String key = "view:" + blogId + ":" + sessionId;
        Boolean added = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofMinutes(30));
        if (Boolean.TRUE.equals(added)) {
            redisTemplate.opsForSet().add(VIEWS_TO_FLUSH_KEY, String.valueOf(blogId));
            return true;
        }
        return false;
    }

    @Override
    public long flushViewsToDb() {
        Set<String> blogIds = redisTemplate.opsForSet().members(VIEWS_TO_FLUSH_KEY);
        if (blogIds == null || blogIds.isEmpty()) {
            return 0;
        }
        long total = 0;
        for (String blogIdStr : blogIds) {
            Long blogId = Long.valueOf(blogIdStr);
            redisTemplate.opsForSet().remove(VIEWS_TO_FLUSH_KEY, blogIdStr);
            String pattern = "view:" + blogId + ":*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                long uniqueViews = keys.size();
                blogRepository.incrementViewsBy(blogId, uniqueViews);
                total += uniqueViews;
                redisTemplate.delete(keys);
            }
        }
        log.info("Flushed {} views for {} blogs", total, blogIds.size());
        return total;
    }
}
