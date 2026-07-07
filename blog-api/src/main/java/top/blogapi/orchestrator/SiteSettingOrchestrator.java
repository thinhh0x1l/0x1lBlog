package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.service.site.SiteSettingService;

import java.util.List;

/**
 * Điều phối truy xuất và cập nhật hàng loạt cài đặt toàn trang.
 */
@Component
@RequiredArgsConstructor
public class SiteSettingOrchestrator {

    private final SiteSettingService siteSettingService;

    public List<SiteSetting> getAll() {
        return siteSettingService.findAll();
    }

    @Transactional
    public void updateAll(List<SiteSetting> settings) {
        settings.forEach(siteSettingService::upsert);
    }
}
