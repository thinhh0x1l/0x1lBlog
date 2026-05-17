package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.repository.SiteSettingRepository;
import top.blogapi.service.SiteSettingService;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class SiteSettingServiceImpl implements SiteSettingService {

    SiteSettingRepository siteSettingRepository;

    @Transactional(readOnly = true)
    @Override
    public List<SiteSetting> getList() {
        return siteSettingRepository.getList();
    }

    @Transactional
    @Override
    public void updateSiteSetting(List<SiteSetting> siteSettings) {
        if(siteSettingRepository.updateAll(siteSettings) == 0)
            throw new AppException(ErrorCode.SITE_SETTINGS_NOT_FOUND);
    }

    @Transactional
    @Override
    public void deleteSettingById(List<Long> ids) {

        if(siteSettingRepository.deleteBatch(ids)==0)
            throw new AppException(ErrorCode.SITE_SETTINGS_NOT_FOUND);
    }

    @Transactional
    @Override
    public void saveSiteSetting(List<SiteSetting>  siteSettings) {
        if(siteSettingRepository.saveBatch(siteSettings) == 0)
            throw new AppException(ErrorCode.SITE_SETTINGS_NOT_FOUND);
    }

    @Override
    public List<SiteSetting> getMp3Setting(int type) {
        return siteSettingRepository.mp3Setting(type);

    }
}
