package top.blogapi.gamification.reputation.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.reputation.domain.entity.UserExpLog;

import java.util.List;

@Repository
public interface UserExpLogJpaRepository extends JpaRepository<UserExpLog, Long> {

    @Query(value = "SELECT * FROM user_exp_log WHERE user_id = :userId ORDER BY created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<UserExpLog> findByUserId(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM user_exp_log WHERE user_id = :userId", nativeQuery = true)
    long sumExpByUserId(@Param("userId") Long userId);
}
