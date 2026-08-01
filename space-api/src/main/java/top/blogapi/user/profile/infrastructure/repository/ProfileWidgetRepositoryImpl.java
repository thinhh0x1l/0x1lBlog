package top.blogapi.user.profile.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.user.profile.domain.entity.ProfileWidget;
import top.blogapi.user.profile.domain.repository.ProfileWidgetRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileWidgetRepositoryImpl implements ProfileWidgetRepository {

    private final ProfileWidgetJpaRepository jpaAdapter;

    @Override
    public List<ProfileWidget> findByUserIdOrderBySortOrder(Long userId) {
        return jpaAdapter.findByUserIdOrderBySortOrder(userId);
    }

    @Override
    public Optional<ProfileWidget> findByUserIdAndWidgetType(Long userId, String widgetType) {
        return jpaAdapter.findByUserIdAndWidgetType(userId, widgetType);
    }

    @Override
    public void insert(ProfileWidget widget) {
        Instant now = Instant.now();
        widget.setCreatedAt(now);
        widget.setUpdatedAt(now);
        jpaAdapter.save(widget);
    }

    @Override
    public void update(ProfileWidget widget) {
        widget.setUpdatedAt(Instant.now());
        jpaAdapter.save(widget);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaAdapter.deleteByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        jpaAdapter.deleteById(id);
    }

    @Override
    public boolean existsByUserIdAndWidgetType(Long userId, String widgetType) {
        return jpaAdapter.existsByUserIdAndWidgetType(userId, widgetType);
    }

    @Override
    public void updateSortOrder(Long id, Integer sortOrder) {
        jpaAdapter.updateSortOrderById(id, sortOrder);
    }
}
