package top.blogapi.service.site.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.repository.SiteSettingRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.cache.CacheKey;
import top.blogapi.service.cache.CachePolicies;
import top.blogapi.service.site.SiteSettingService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai SiteSettingService với tích hợp cache, cung cấp
 * CRUD cài đặt key-value và xóa cache hàng loạt khi cập nhật.
 */
public class SiteSettingServiceImpl implements SiteSettingService {

    private final SiteSettingRepository siteSettingRepository;
    private final CacheService cacheService;

    @Override
    public List<SiteSetting> findAll() {
        return cacheService.get(
                CacheKey.siteSettingAll(),
                (Class<List<SiteSetting>>) (Class<?>) List.class,
                () -> siteSettingRepository.findAll(),
                CachePolicies.SITE_SETTING
        );
    }

    @Override
    public SiteSetting findByKey(String key) {
        return cacheService.get(
                CacheKey.siteSettingByKey(key),
                SiteSetting.class,
                () -> siteSettingRepository.findByKey(key).orElse(null),
                CachePolicies.SITE_SETTING
        );
    }

    @Override
    public void upsert(SiteSetting setting) {
        siteSettingRepository.upsert(setting);
        cacheService.evict(CacheKey.siteSettingAll());
        cacheService.evict(CacheKey.siteSettingByKey(setting.getKey()));
    }

    @Override
    public void delete(Long id) {
        siteSettingRepository.delete(id);
        cacheService.evict(CacheKey.siteSettingAll());
    }
}
