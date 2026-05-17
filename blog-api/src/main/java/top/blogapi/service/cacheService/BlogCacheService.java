package top.blogapi.service.cacheService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.blogapi.config.CacheNameConfig;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.model.vo.BlogDetail;
import top.blogapi.model.vo.BlogTagsInfo;
import top.blogapi.model.vo.PageResult;
import top.blogapi.service.BlogService;
import top.blogapi.service._zing_mp3.MusicService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogCacheService {

    BlogService blogService;
    MusicService musicService;

    BlogMapper blogMapper;

    Map<Long, Long> viewsDbCache = new ConcurrentHashMap<>();
    Map<Long, AtomicLong> pendingCache = new ConcurrentHashMap<>();


    public long getViews(Long blogId) {
        Long dbViews = viewsDbCache.get(blogId);

        if (dbViews == null) {
            dbViews = blogService.getViewsByBlogId(blogId);
            viewsDbCache.put(blogId, dbViews);
        }

        long pending = pendingCache
                .computeIfAbsent(blogId, k -> new AtomicLong(0))
                .get();

        return dbViews + pending;
    }

    public void increase(Long blogId) {
        long value = pendingCache
                .computeIfAbsent(blogId, k -> new AtomicLong(0))
                .incrementAndGet();
        log.debug("Blog {} pending +{}", blogId, value);
    }

    public Map<Long, AtomicLong> getAllPending() {
        return pendingCache;
    }

    public void addDbViews(Long blogId, long delta) {
        viewsDbCache.merge(blogId, delta, Long::sum);
    }

    public long getPending(Long blogId) {
        AtomicLong counter = pendingCache.get(blogId);
        return counter == null ? 0 : counter.get();
    }

    @Cacheable(
            value = CacheNameConfig.BLOG_DETAILS,
            key = "#id"
    )
    public BlogDetail getBlogByIdAndIsPublished(Long id){

        return blogService.getBlogByIdAndIsPublished(id);
    }

    @Cacheable(
            value = CacheNameConfig.HOME_BLOG_INFO_LIST,
            key = "#pageNum",
            unless = "#result == null"
    )
    public PageResult<BlogInfo> getFromCacheOrDb(Integer pageNum){

        System.out.println(">>> QUERY DATABASE");
        String orderBy = "is_top desc, create_time desc";
        PageHelper.startPage(pageNum, 5, orderBy);
        PageInfo<BlogTagsInfo> pageInfo =
                new PageInfo<>(blogService.getBlogInfoListByIsPublished());

        if (pageInfo.getList().isEmpty())
            return null;

        return PageResult.from(
                pageInfo.convert(blogMapper::toBlogsResponse)
        );
    }
}

