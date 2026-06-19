package top.blogapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import top.blogapi.dto.response.BlogResponse;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.dto.mapper.BlogMapper;
import top.blogapi.dto.mapper.UserMapper;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.User;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheService {

    private static final String BLOG_CACHE = "blogs";
    private static final String USER_CACHE = "users";

    private final CacheManager cacheManager;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;
    private final UserMapper userMapper;

    public Optional<BlogResponse> getBlogResponse(Long id) {
        Cache cache = cacheManager.getCache(BLOG_CACHE);
        if (cache != null) {
            BlogResponse cached = cache.get("blog:" + id, BlogResponse.class);
            if (cached != null) {
                return Optional.of(cached);
            }
        }
        return blogRepository.findById(id)
                .map(blogMapper::toResponse)
                .map(response -> {
                    if (cache != null) {
                        cache.put("blog:" + id, response);
                    }
                    return response;
                });
    }

    public Optional<UserResponse> getUserResponse(Long id) {
        Cache cache = cacheManager.getCache(USER_CACHE);
        if (cache != null) {
            UserResponse cached = cache.get("user:" + id, UserResponse.class);
            if (cached != null) {
                return Optional.of(cached);
            }
        }
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .map(response -> {
                    if (cache != null) {
                        cache.put("user:" + id, response);
                    }
                    return response;
                });
    }

    public void evictBlog(Long id) {
        Cache cache = cacheManager.getCache(BLOG_CACHE);
        if (cache != null) {
            cache.evict("blog:" + id);
        }
    }

    public void evictUser(Long id) {
        Cache cache = cacheManager.getCache(USER_CACHE);
        if (cache != null) {
            cache.evict("user:" + id);
        }
    }

    public void evictAllBlogs() {
        Cache cache = cacheManager.getCache(BLOG_CACHE);
        if (cache != null) {
            cache.clear();
        }
    }
}
