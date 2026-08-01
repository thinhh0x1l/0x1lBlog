package top.blogapi.content.blog.domain.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ViewTrackingService {

    private final Map<String, Long> viewCache = new ConcurrentHashMap<>();

    public boolean isUniqueView(String sessionId, Long blogId) {
        String key = sessionId + ":" + blogId;
        Long lastView = viewCache.get(key);
        long now = System.currentTimeMillis();
        if (lastView != null && now - lastView < 30 * 60 * 1000L) {
            return false;
        }
        viewCache.put(key, now);
        return true;
    }
}
