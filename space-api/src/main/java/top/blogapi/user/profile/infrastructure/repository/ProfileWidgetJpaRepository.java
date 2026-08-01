package top.blogapi.user.profile.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.user.profile.domain.entity.ProfileWidget;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileWidgetJpaRepository extends JpaRepository<ProfileWidget, Long> {

    List<ProfileWidget> findByUserIdOrderBySortOrder(Long userId);

    Optional<ProfileWidget> findByUserIdAndWidgetType(Long userId, String widgetType);

    void deleteByUserId(Long userId);

    void deleteById(Long id);

    boolean existsByUserIdAndWidgetType(Long userId, String widgetType);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileWidget pw SET pw.sortOrder = :sortOrder, pw.updatedAt = CURRENT_TIMESTAMP WHERE pw.id = :id")
    void updateSortOrderById(Long id, Integer sortOrder);
}
