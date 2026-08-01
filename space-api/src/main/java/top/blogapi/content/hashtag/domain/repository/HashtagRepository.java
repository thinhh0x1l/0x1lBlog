package top.blogapi.content.hashtag.domain.repository;

import top.blogapi.content.hashtag.domain.entity.Hashtag;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {
    Optional<Hashtag> findById(Long id);
    Optional<Hashtag> findByName(String name);
    List<Hashtag> findTop(int limit);
    void save(Hashtag hashtag);
    void incrementUsage(Long id);
    void decrementUsage(Long id);
    boolean existsByName(String name);
}
