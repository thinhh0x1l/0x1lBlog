package top.blogapi.dto.request.siteSetting;

import lombok.Data;
import top.blogapi.model.entity.SiteSetting;

import java.util.List;

@Data
public class SiteSettingUpdateReq {
    List<Long> deleteIds;
    List<SiteSetting> settings;
}
