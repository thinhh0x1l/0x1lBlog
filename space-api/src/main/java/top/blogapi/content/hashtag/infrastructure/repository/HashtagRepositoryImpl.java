package top.blogapi.content.hashtag.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.hashtag.domain.entity.Hashtag;
import top.blogapi.content.hashtag.domain.repository.HashtagRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepository {

    private final HashtagJpaRepository jpa;

    @Override
    public Optional<Hashtag> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Hashtag> findByName(String name) {
        return jpa.findByName(name);
    }

    @Override
    public List<Hashtag> findTop(int limit) {
        return jpa.findTopByOrderByUsageCountDesc(limit);
    }

    @Override
    public void save(Hashtag hashtag) {
        jpa.save(hashtag);
    }

    @Override
    public void incrementUsage(Long id) {
        jpa.incrementUsage(id);
    }

    @Override
    public void decrementUsage(Long id) {
        jpa.decrementUsage(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }
}
