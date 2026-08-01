package top.blogapi.gamification.blind.domain.repository;

import top.blogapi.gamification.blind.domain.entity.BlindChallenge;

import java.time.LocalDate;
import java.util.Optional;

public interface BlindChallengeRepository {

    Optional<BlindChallenge> findByDate(LocalDate date);

    Optional<BlindChallenge> findTopByOrderByDateDesc();

    void save(BlindChallenge challenge);

    void reveal(Long id);
}
