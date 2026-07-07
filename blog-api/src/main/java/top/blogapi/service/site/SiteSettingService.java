package top.blogapi.service.site;

import top.blogapi.model.entity.SiteSetting;

import java.util.List;

/**
 * Giao diện service cho cấu hình toàn trang, cung cấp thao tác CRUD
 * với tích hợp cache cho cài đặt key-value.
 */
public interface SiteSettingService {
    List<SiteSetting> findAll();
    SiteSetting findByKey(String key);
    void upsert(SiteSetting setting);
    void delete(Long id);
}
