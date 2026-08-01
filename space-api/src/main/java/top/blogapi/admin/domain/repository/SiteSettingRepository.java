package top.blogapi.admin.domain.repository;

import top.blogapi.admin.domain.entity.SiteSetting;
import java.util.List;
import java.util.Optional;

public interface SiteSettingRepository {
    Optional<SiteSetting> findById(Long id);
    Optional<SiteSetting> findByKey(String key);
    List<SiteSetting> findAll();
    void save(SiteSetting setting);
    void delete(Long id);
}
