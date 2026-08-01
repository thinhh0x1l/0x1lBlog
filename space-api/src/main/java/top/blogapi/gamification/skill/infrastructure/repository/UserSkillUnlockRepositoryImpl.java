package top.blogapi.gamification.skill.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.skill.domain.entity.UserSkillUnlock;
import top.blogapi.gamification.skill.domain.repository.UserSkillUnlockRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSkillUnlockRepositoryImpl implements UserSkillUnlockRepository {

    private final UserSkillUnlockJpaRepository jpa;

    @Override
    public Optional<UserSkillUnlock> findByUserIdAndSkillId(Long userId, Long skillId) {
        return jpa.findByUserIdAndSkillId(userId, skillId);
    }

    @Override
    public List<UserSkillUnlock> findByUserId(Long userId) {
        return jpa.findByUserId(userId);
    }

    @Override
    public boolean existsByUserIdAndSkillId(Long userId, Long skillId) {
        return jpa.existsByUserIdAndSkillId(userId, skillId);
    }

    @Override
    public void save(UserSkillUnlock unlock) {
        if (unlock.getUnlockedAt() == null) {
            unlock.setUnlockedAt(Instant.now());
        }
        jpa.save(unlock);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpa.deleteByUserId(userId);
    }
}
