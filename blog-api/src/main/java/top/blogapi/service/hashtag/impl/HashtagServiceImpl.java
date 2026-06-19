package top.blogapi.service.hashtag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Hashtag;
import top.blogapi.repository.HashtagRepository;
import top.blogapi.service.hashtag.HashtagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    @Cacheable(value = "hashtags", key = "'findById:' + #id")
    public Hashtag findById(Long id) {
        return hashtagRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_FOUND));
    }

    @Override
    @Transactional
    @CacheEvict(value = "hashtags", allEntries = true)
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
    @Cacheable(value = "hashtags", key = "'getTop:' + #limit")
    public List<Hashtag> getTopHashtags(int limit) {
        return hashtagRepository.findTop(limit);
    }
}
