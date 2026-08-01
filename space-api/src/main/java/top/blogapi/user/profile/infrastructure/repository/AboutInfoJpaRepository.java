package top.blogapi.user.profile.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.blogapi.user.profile.domain.entity.AboutInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface AboutInfoJpaRepository extends JpaRepository<AboutInfo, Long> {

    @Query("SELECT ai FROM AboutInfo ai WHERE ai.type = :type ORDER BY ai.updatedAt DESC")
    Optional<AboutInfo> findFirstByTypeOrderByUpdatedAtDesc(String type);
}
