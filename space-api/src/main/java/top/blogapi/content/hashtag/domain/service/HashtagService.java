package top.blogapi.content.hashtag.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.content.hashtag.domain.entity.Hashtag;
import top.blogapi.content.hashtag.domain.repository.HashtagRepository;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;
import top.blogapi.infra.cache.CachePolicies;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final CacheService cacheService;

    public Hashtag findById(Long id) {
        return cacheService.get(
                CacheKey.tag(id),
                Hashtag.class,
                () -> hashtagRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_FOUND)),
                CachePolicies.TAG
        );
    }

    public Hashtag findOrCreate(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> {
                    Hashtag tag = new Hashtag();
                    tag.setName(name);
                    hashtagRepository.save(tag);
                    return tag;
                });
    }

    public List<Hashtag> getTopHashtags(int limit) {
        return hashtagRepository.findTop(limit);
    }
}
