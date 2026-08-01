package top.blogapi.gamification.blind.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.blind.domain.entity.BlindChallenge;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BlindChallengeJpaRepository extends JpaRepository<BlindChallenge, Long> {

    Optional<BlindChallenge> findByDate(LocalDate date);

    @Query(value = "SELECT * FROM blind_challenges ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Optional<BlindChallenge> findTopByOrderByDateDesc();

    @Modifying
    @Query(value = "UPDATE blind_challenges SET revealed = TRUE WHERE id = :id", nativeQuery = true)
    void reveal(@Param("id") Long id);
}
