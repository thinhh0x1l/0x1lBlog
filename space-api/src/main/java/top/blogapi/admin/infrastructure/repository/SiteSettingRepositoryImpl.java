package top.blogapi.admin.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.admin.domain.entity.SiteSetting;
import top.blogapi.admin.domain.repository.SiteSettingRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SiteSettingRepositoryImpl implements SiteSettingRepository {

    private final SiteSettingJpaRepository jpa;

    @Override
    public Optional<SiteSetting> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<SiteSetting> findByKey(String key) {
        return jpa.findByKey(key);
    }

    @Override
    public List<SiteSetting> findAll() {
        return jpa.findAllByOrderByKey();
    }

    @Override
    public void save(SiteSetting setting) {
        jpa.upsert(setting);
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }
}
