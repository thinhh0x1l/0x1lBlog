package top.blogapi.user.profile.domain.repository;

import top.blogapi.user.profile.domain.entity.ProfileWidget;

import java.util.List;
import java.util.Optional;

public interface ProfileWidgetRepository {

    List<ProfileWidget> findByUserIdOrderBySortOrder(Long userId);

    Optional<ProfileWidget> findByUserIdAndWidgetType(Long userId, String widgetType);

    void insert(ProfileWidget widget);

    void update(ProfileWidget widget);

    void deleteByUserId(Long userId);

    void deleteById(Long id);

    boolean existsByUserIdAndWidgetType(Long userId, String widgetType);

    void updateSortOrder(Long id, Integer sortOrder);
}
