package top.blogapi.service.site.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.repository.SiteSettingRepository;
import top.blogapi.service.site.SiteSettingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteSettingServiceImpl implements SiteSettingService {

    private final SiteSettingRepository siteSettingRepository;

    @Override
    @Cacheable(value = "siteSettings", key = "'findAll'")
    public List<SiteSetting> findAll() {
        return siteSettingRepository.findAll();
    }

    @Override
    @Cacheable(value = "siteSettings", key = "'findByKey:' + #key")
    public SiteSetting findByKey(String key) {
        return siteSettingRepository.findByKey(key).orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(value = "siteSettings", allEntries = true)
    public void upsert(SiteSetting setting) {
        siteSettingRepository.upsert(setting);
    }

    @Override
    @Transactional
    @CacheEvict(value = "siteSettings", allEntries = true)
    public void delete(Long id) {
        siteSettingRepository.delete(id);
    }
}
