package top.blogapi.service.counter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.blogapi.repository.BlogRepository;
import top.blogapi.service.counter.CounterService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai CounterService sử dụng Redis để đếm lượt xem tạm thời
 * với đồng bộ lô bất đồng bộ xuống cơ sở dữ liệu.
 */
public class CounterServiceImpl implements CounterService {

    private final RedisTemplate<String, String> redisTemplate;
    private final BlogRepository blogRepository;

    private static final String VIEW_KEY_PREFIX = "blog:";
    private static final String VIEW_KEY_SUFFIX = ":views";
    private static final long VIEW_TTL_HOURS = 24;

    @Override
    @Async
    public void incrementView(Long blogId) {
        String key = VIEW_KEY_PREFIX + blogId + VIEW_KEY_SUFFIX;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, VIEW_TTL_HOURS, TimeUnit.HOURS);
    }

    @Override
    public Long getViewCount(Long blogId) {
        String key = VIEW_KEY_PREFIX + blogId + VIEW_KEY_SUFFIX;
        String val = redisTemplate.opsForValue().get(key);
        return val != null ? Long.parseLong(val) : 0L;
    }

    @Override
    @Async
    public void syncViewToDb(Long blogId) {
        String key = VIEW_KEY_PREFIX + blogId + VIEW_KEY_SUFFIX;
        String val = redisTemplate.opsForValue().get(key);
        if (val != null) {
            long count = Long.parseLong(val);
            if (count > 0) {
                blogRepository.incrementViewsBy(blogId, count);
                redisTemplate.delete(key);
            }
        }
    }
}
