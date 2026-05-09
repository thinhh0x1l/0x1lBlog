package top.blogapi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response.category.CategorySlug;
import top.blogapi.model.vo.PageResult;
import top.blogapi.service.RedisService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class RedisServiceImpl implements RedisService {
    ObjectMapper objectMapper;
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult<BlogInfo> getBlogInfoPageResultByHash(String hash, Integer pageNum) {
        Object redisResult = redisTemplate.opsForHash().get(hash, pageNum.toString());
        if(redisResult != null) log.error("redis");
        return objectMapper.convertValue(
                redisResult,
                objectMapper.getTypeFactory()
                        .constructParametricType(PageResult.class,BlogInfo.class)
        );
    }

    @Override
    public void saveBlogInfoPageResultToHash(String hash, Integer pageNum, Object object) {
        redisTemplate.opsForHash().put(hash, pageNum.toString(), object);
        redisTemplate.expire(hash, 10, TimeUnit.MINUTES);
    }

    @Override
    public <T> List<T> getListByValue(String key, TypeReference<List<T>> typeReference) {
        Object value = redisTemplate.opsForValue().get(key);
        if(value != null) log.error("redis");
        return objectMapper.convertValue(value, typeReference);
    }

    @Override
    public <T> void saveListToValue(String key, List<T> list) {
        redisTemplate.opsForValue().set(key, list);
    }

    @Override
    public <T> Map<String, T> getMapByValue(String key, TypeReference<Map<String, T>> mapTypeReference) {
        Object value = redisTemplate.opsForValue().get(key);
        if(value != null) log.error("redis");
        return objectMapper.convertValue(value, mapTypeReference);
    }

    @Override
    public <T> void saveMapToValue(String key, Map<String, T> map) {
        redisTemplate.opsForValue().set(key, map);
    }

    @Override
    public <T> T getObjectByValue(String key, Class<T> t) {
        Object redisResult = redisTemplate.opsForValue().get(key);
        if(redisResult != null) log.error("redis");
        return (T) objectMapper.convertValue(redisResult, t);
    }

    @Override
    public void saveObjectToValue(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

}
