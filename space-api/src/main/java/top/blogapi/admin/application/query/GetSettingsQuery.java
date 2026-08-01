package top.blogapi.admin.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.admin.domain.entity.SiteSetting;
import top.blogapi.admin.domain.service.SiteSettingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSettingsQuery {

    private final SiteSettingService siteSettingService;

    public List<SiteSetting> execute() {
        return siteSettingService.findAll();
    }
}
