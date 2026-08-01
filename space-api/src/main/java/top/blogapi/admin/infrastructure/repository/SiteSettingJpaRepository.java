package top.blogapi.admin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.admin.domain.entity.SiteSetting;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteSettingJpaRepository extends JpaRepository<SiteSetting, Long> {

    Optional<SiteSetting> findByKey(String key);

    List<SiteSetting> findAllByOrderByKey();

    @Modifying
    @Query(value = """
            INSERT INTO site_settings (key, value, type, description)
            VALUES (:#{#s.key}, :#{#s.value}, :#{#s.type}, :#{#s.description})
            ON CONFLICT (key) DO UPDATE SET value = :#{#s.value}, updated_at = NOW()
            """, nativeQuery = true)
    void upsert(@Param("s") SiteSetting setting);
}
