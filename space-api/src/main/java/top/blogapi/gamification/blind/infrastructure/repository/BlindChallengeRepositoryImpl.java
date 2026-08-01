package top.blogapi.gamification.blind.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.blind.domain.entity.BlindChallenge;
import top.blogapi.gamification.blind.domain.repository.BlindChallengeRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlindChallengeRepositoryImpl implements BlindChallengeRepository {

    private final BlindChallengeJpaRepository jpa;

    @Override
    public Optional<BlindChallenge> findByDate(LocalDate date) {
        return jpa.findByDate(date);
    }

    @Override
    public Optional<BlindChallenge> findTopByOrderByDateDesc() {
        return jpa.findTopByOrderByDateDesc();
    }

    @Override
    public void save(BlindChallenge challenge) {
        if (challenge.getCreatedAt() == null) {
            challenge.setCreatedAt(Instant.now());
        }
        jpa.save(challenge);
    }

    @Override
    public void reveal(Long id) {
        jpa.reveal(id);
    }
}
