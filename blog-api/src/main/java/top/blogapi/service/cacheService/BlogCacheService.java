package top.blogapi.service.cacheService;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.blogapi.config.CacheNameConfig;
import top.blogapi.model.vo.BlogDetail;
import top.blogapi.service.BlogService;
import top.blogapi.service._zing_mp3.MusicService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogCacheService {
    BlogService blogService;
    MusicService musicService;

    Cache<Long, AtomicLong> viewCache;

    public void increase(Long blogId) {
        long delta = viewCache.asMap()
                .computeIfAbsent(blogId, id -> new AtomicLong())
                .incrementAndGet();
        System.out.println("Blog " +blogId +" tăng " + delta);
    }

    public long getPending(Long blogId) {
        AtomicLong counter = viewCache.getIfPresent(blogId);
        return counter == null ? 0 : counter.get();
    }


    public Map<Long, AtomicLong> getAll() {
        return viewCache.asMap();
    }

    @Cacheable(
            value = CacheNameConfig.BLOG_DETAILS,
            key = "#id"
    )
    public BlogDetail getBlogByIdAndIsPublished(Long id){

        long start = System.currentTimeMillis();

        BlogDetail blogDetail =
                blogService.getBlogByIdAndIsPublished(id);



        log.info("DB query: {} ms",
                System.currentTimeMillis() - start);

        if(blogDetail.getMusicId() != null){

            start = System.currentTimeMillis();

            try {
                blogDetail.setMusicInfo(
                        musicService.getCompleteSongData(
                                blogDetail.getMusicId(),
                                2
                        )
                );

            } catch (Exception e){

                log.error("Error load music info", e);

                blogDetail.setMusicInfo(null);
            }

            log.info("Music API: {} ms",
                    System.currentTimeMillis() - start);
        }

        return blogDetail;
    }
}

