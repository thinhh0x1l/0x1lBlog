package top.blogapi.service.site;

import top.blogapi.model.entity.SiteSetting;

import java.util.List;

public interface SiteSettingService {
    List<SiteSetting> findAll();
    SiteSetting findByKey(String key);
    void upsert(SiteSetting setting);
    void delete(Long id);
}
