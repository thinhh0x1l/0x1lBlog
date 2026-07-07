package top.blogapi.service.hashtag.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Hashtag;
import top.blogapi.repository.HashtagRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.cache.CachePolicies;
import top.blogapi.service.hashtag.HashtagService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai HashtagService với cache, cung cấp ngữ nghĩa
 * tìm-hoặc-tạo và truy xuất hashtag thịnh hành.
 */
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final CacheService cacheService;

    @Override
    public Hashtag findById(Long id) {
        return cacheService.get(
                CacheKey.tag(id),
                Hashtag.class,
                () -> hashtagRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_FOUND)),
                CachePolicies.TAG
        );
    }

    @Override
    public Hashtag findOrCreate(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> {
                    Hashtag tag = new Hashtag();
                    tag.setName(name);
                    hashtagRepository.insert(tag);
                    return tag;
                });
    }

    @Override
    public List<Hashtag> getTopHashtags(int limit) {
        return hashtagRepository.findTop(limit);
    }
}
