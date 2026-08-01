package top.blogapi.admin.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.admin.domain.entity.SiteSetting;
import top.blogapi.admin.domain.repository.SiteSettingRepository;
import top.blogapi.infra.cache.CacheService;
import top.blogapi.infra.cache.CacheKey;
import top.blogapi.infra.cache.CachePolicies;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteSettingService {

    private final SiteSettingRepository siteSettingRepository;
    private final CacheService cacheService;

    public List<SiteSetting> findAll() {
        return cacheService.get(
                CacheKey.siteSettingAll(),
                (Class<List<SiteSetting>>) (Class<?>) List.class,
                () -> siteSettingRepository.findAll(),
                CachePolicies.SITE_SETTING
        );
    }

    public SiteSetting findByKey(String key) {
        return cacheService.get(
                CacheKey.siteSettingByKey(key),
                SiteSetting.class,
                () -> siteSettingRepository.findByKey(key).orElse(null),
                CachePolicies.SITE_SETTING
        );
    }

    public void save(SiteSetting setting) {
        siteSettingRepository.save(setting);
        cacheService.evict(CacheKey.siteSettingAll());
        cacheService.evict(CacheKey.siteSettingByKey(setting.getKey()));
    }

    public void delete(Long id) {
        siteSettingRepository.delete(id);
        cacheService.evict(CacheKey.siteSettingAll());
    }
}
