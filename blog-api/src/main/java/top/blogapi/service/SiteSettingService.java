package top.blogapi.service;

import top.blogapi.model.entity.SiteSetting;

import java.util.List;
import java.util.Map;

public interface SiteSettingService {
    List<SiteSetting> getList();

    void updateSiteSetting(List<SiteSetting> siteSetting);

    void deleteSettingById(List<Long> ids);

    void saveSiteSetting(List<SiteSetting> siteSetting);

    List<SiteSetting> getMp3Setting(int type);
}
