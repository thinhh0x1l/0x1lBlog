package top.blogapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.model.vo.PageResult;
import top.blogapi.service.RedisService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class RedisServiceImpl implements RedisService {
    ObjectMapper objectMapper;
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult<BlogInfo> getPageResultByHash(String hash, Integer pageNum) {
        Object redisResult = redisTemplate.opsForHash().get(hash, pageNum.toString());
        if(redisResult == null) return null;
        return objectMapper.convertValue(
                redisResult,
                objectMapper.getTypeFactory()
                        .constructParametricType(PageResult.class,BlogInfo.class)
        );
    }

    @Override
    public void setPageResultToHash(String hash, Integer pageNum, Object object) {
        redisTemplate.opsForHash().put(hash, pageNum.toString(), object);
        redisTemplate.expire(hash, 10, TimeUnit.MINUTES);
    }
}
