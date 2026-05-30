package top.blogapi.service.impl.orchestration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.internal.BadgeInternal;
import top.blogapi.dto.internal.FavoriteInternal;
import top.blogapi.dto.internal.IntroductionInternal;
import top.blogapi.dto.request.siteSetting.SiteSettingUpdateReq;
import top.blogapi.service._zing_mp3.Mp3Service;
import top.blogapi.constant.CacheNameConstant;
import top.blogapi.model.enums.TypeSetting;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.dto.internal.CopyrightInternal;
import top.blogapi.service.SiteSettingService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static top.blogapi.model.enums.TypeSetting.*;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SiteSettingOrchestrator {
    SiteSettingService siteSettingService;

    Mp3Service mp3Service;

    ObjectMapper objectMapper;

   public Map<String, List<SiteSetting>> getList(){
       return siteSettingService.getList().stream()
               .collect(Collectors.groupingBy(siteSetting -> "type" + siteSetting.getType()));
   }

    @Cacheable(value = CacheNameConstant.SITE_INFO_MAP)
    public Map<String, Object> getSiteInfo() {
        List<SiteSetting> siteSettings = siteSettingService.getList();

        Map<Integer, List<SiteSetting>> groupedByType =
                siteSettings.stream()
                        .collect(Collectors.groupingBy(SiteSetting::getType));

        Map<String, Object> siteInfoMap =
                processSiteInfo(groupedByType.getOrDefault(TYPE_SITE_INFO.getType(), List.of()));

        List<BadgeInternal> badgeInternals =
                processBadges(groupedByType.getOrDefault(TypeSetting.TYPE_BADGE.getType(), List.of()));

        IntroductionInternal introductionInternal =
                processIntroduction(groupedByType.getOrDefault(TYPE_INTRODUCTION.getType(), List.of()));

        Map<String, Object> result = new HashMap<>();
        result.put("siteInfo", siteInfoMap);
        result.put("badges", badgeInternals);
        result.put("introduction", introductionInternal);

        return result;
    }


    /// Xử lý thông tin site (type 1)
    private Map<String, Object> processSiteInfo(List<SiteSetting> siteInfos)  {
        Map<String, Object> result = new HashMap<>();
        for(SiteSetting info : siteInfos){
            if("copyright".equals(info.getNameEn()))
                try {
                    result.put(info.getNameEn(), objectMapper.readValue(info.getValue(), CopyrightInternal.class));
                }catch (JsonProcessingException ex){
                    log.warn("Lỗi parse info: {}", info.getValue());
                }
            else
                result.put(info.getNameEn(), info.getValue());
        }
        return result;
    }

    /// Xử lý BadgeInternal (type 2)
    private List<BadgeInternal> processBadges(List<SiteSetting> badgeSettings){
        return badgeSettings.stream()
                .map(setting -> {
                    try {
                        return objectMapper.readValue(setting.getValue(), BadgeInternal.class);
                    } catch (JsonProcessingException e) {
                        log.warn("Lỗi parse BadgeInternal: {}", setting.getValue());
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /// Xử lý thông tin giới thiệu (type 3)
    private IntroductionInternal processIntroduction(List<SiteSetting> introSettings){
        IntroductionInternal introductionInternal = new IntroductionInternal();
        List<FavoriteInternal> favoriteInternals = new ArrayList<>();
        List<String> rollTexts = new ArrayList<>();
        for (SiteSetting info : introSettings) {
            processIntroField(info, introductionInternal, favoriteInternals, rollTexts);
        }
        introductionInternal.setFavorites(favoriteInternals);
        introductionInternal.setRollText(rollTexts);
        return introductionInternal;
    }

    public Map<String, Map<String, Object>> loadConfig() {
        Map<String, Map<String, Object>> map = new HashMap<>();

        siteSettingService.getMp3Setting(TYPE_MP3.getType())
                .forEach(siteSetting -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(siteSetting.getValue());
                        Map<String, Object> nodeMap = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
                        map.put(siteSetting.getNameEn(), nodeMap);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        return map;
    }

    @PostConstruct
    private void init() {
        Map<String, Map<String, Object>> map = loadConfig();
        mp3Service.setConfigFromDb(map);
    }
    public void reloadConfig() {
        Map<String, Map<String, Object>> map = loadConfig();
        mp3Service.setConfigFromDb(map);
    }
    /// Xử lý từ Field trong IntroductionInternal
    private void processIntroField(SiteSetting siteSetting, IntroductionInternal intro,
                                   List<FavoriteInternal> favoriteInternals, List<String> rollTexts){
        String nameEn = siteSetting.getNameEn();
        String value = siteSetting.getValue();
        FavoriteInternal favoriteInternal = null;
        if(nameEn.equals("favorite")){
            try {
                favoriteInternal = objectMapper.readValue(value, FavoriteInternal.class);
            }catch (JsonProcessingException ex){
                log.warn("Lỗi parse favorite: {}", value);
                nameEn = "";
            }
        }


        switch (nameEn){
            case "avatar" -> intro.setAvatar(value);
            case "name" -> intro.setName(value);
            case "github" -> intro.setGithub(value);
            case "email" -> intro.setEmail(value);
            case "facebook" -> intro.setFacebook(value);
            case "leetCode" -> intro.setLeetCode(value);
            case "instagram" -> intro.setInstagram(value);
            case "favorite" -> favoriteInternals.add(favoriteInternal);
            case "rollText" -> rollTexts.addAll(extractRollTexts(value));
        }
    }

    /// Trích xuất Roll Texts
    private List<String> extractRollTexts(String value){
        Pattern pattern = Pattern.compile("\"(.*?)\""); // Bắt pattern dạng 'text?'
        Matcher matcher = pattern.matcher(value);
        List<String> texts = new ArrayList<>();
        while(matcher.find())
            texts.add(matcher.group(1)); // lấy từng nhóm '()'
        return texts;
    }

    @CacheEvict(value = CacheNameConstant.SITE_INFO_MAP, allEntries = true)
    public void updateAll(SiteSettingUpdateReq req){
        List<SiteSetting> siteSettings =
                Optional.ofNullable(req.getSettings())
                        .orElse(Collections.emptyList());
        List<Long> deleteIds =
                Optional.ofNullable(req.getDeleteIds())
                        .orElse(Collections.emptyList());
        List<SiteSetting> updates = new ArrayList<>();
        List<SiteSetting> saves = new ArrayList<>();

        for (SiteSetting s : siteSettings)
            if(s.getId() != null) updates.add(s);
            else saves.add(s);

        siteSettingService.saveSiteSetting(saves);
        siteSettingService.updateSiteSetting(updates);
        siteSettingService.deleteSettingById(deleteIds);
    }


}
