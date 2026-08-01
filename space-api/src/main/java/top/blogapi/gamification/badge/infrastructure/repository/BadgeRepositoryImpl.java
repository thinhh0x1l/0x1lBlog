package top.blogapi.gamification.badge.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.badge.domain.entity.Badge;
import top.blogapi.gamification.badge.domain.repository.BadgeRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BadgeRepositoryImpl implements BadgeRepository {

    private final BadgeJpaRepository jpa;

    @Override
    public Optional<Badge> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Badge> findAll() {
        return jpa.findAll();
    }

    @Override
    public void save(Badge badge) {
        if (badge.getCreatedAt() == null) {
            badge.setCreatedAt(Instant.now());
        }
        jpa.save(badge);
    }

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }
}
