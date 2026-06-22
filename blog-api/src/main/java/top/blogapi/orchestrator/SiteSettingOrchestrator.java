package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.service.site.SiteSettingService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SiteSettingOrchestrator {

    private final SiteSettingService siteSettingService;

    public List<SiteSetting> getAll() {
        return siteSettingService.findAll();
    }

    public void updateAll(List<SiteSetting> settings) {
        settings.forEach(siteSettingService::upsert);
    }
}
