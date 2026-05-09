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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.client.zing_mp3.Mp3Service;
import top.blogapi.config.RedisKeyConfig;
import top.blogapi.model.TypeSetting;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.model.vo.Badge;
import top.blogapi.model.vo.Copyright;
import top.blogapi.model.vo.Favorite;
import top.blogapi.model.vo.Introduction;
import top.blogapi.service.RedisService;
import top.blogapi.service.SiteSettingService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static top.blogapi.model.TypeSetting.*;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SiteSettingOrchestrator {
    SiteSettingService siteSettingService;

    Mp3Service mp3Service;
    RedisService redisService;

    ObjectMapper objectMapper;

   public Map<String, List<SiteSetting>> getList(){
       return siteSettingService.getList().stream()
               .collect(Collectors.groupingBy(siteSetting -> "type" + siteSetting.getType()));
   }

   public Map<String, Object> getSiteInfo(){
       String redisKey = RedisKeyConfig.SITE_INFO_MAP;

       Map<String, Object> siteInfoMapFromRedis = redisService.getMapByValue(redisKey, new TypeReference<Map<String, Object>>() {   });
       if (siteInfoMapFromRedis != null)
           return siteInfoMapFromRedis;

       List<SiteSetting> siteSettings = siteSettingService.getList();

       // Phân nhóm theo type
       Map<Integer, List<SiteSetting>> groupedByType = siteSettings.stream()
               .collect(Collectors.groupingBy(SiteSetting::getType));

       // Xử lý từng nhóm
       Map<String, Object> siteInfoMap = processSiteInfo(groupedByType.getOrDefault(TYPE_SITE_INFO.getType(), List.of()));
       List<Badge> badges = processBadges(groupedByType.getOrDefault(TypeSetting.TYPE_BADGE.getType(), List.of()));
       Introduction introduction = processIntroduction(groupedByType.getOrDefault(TYPE_INTRODUCTION.getType(), List.of()));

       Map<String, Object> map = new HashMap<>();
       map.put("siteInfo", siteInfoMap);
       map.put("badges", badges);
       map.put("introduction", introduction);

       redisService.saveMapToValue(redisKey, map);
       return map;
   }


    /// Xử lý thông tin site (type 1)
    private Map<String, Object> processSiteInfo(List<SiteSetting> siteInfos)  {
        Map<String, Object> result = new HashMap<>();
        for(SiteSetting info : siteInfos){
            if("copyright".equals(info.getNameEn()))
                try {
                    result.put(info.getNameEn(), objectMapper.readValue(info.getValue(), Copyright.class));
                }catch (JsonProcessingException ex){
                    log.warn("Lỗi parse info: {}", info.getValue());
                }
            else
                result.put(info.getNameEn(), info.getValue());
        }
        return result;
    }

    /// Xử lý Badge (type 2)
    private List<Badge> processBadges(List<SiteSetting> badgeSettings){
        return badgeSettings.stream()
                .map(setting -> {
                    try {
                        return objectMapper.readValue(setting.getValue(), Badge.class);
                    } catch (JsonProcessingException e) {
                        log.warn("Lỗi parse Badge: {}", setting.getValue());
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /// Xử lý thông tin giới thiệu (type 3)
    private Introduction processIntroduction(List<SiteSetting> introSettings){
        Introduction introduction = new Introduction();
        List<Favorite> favorites = new ArrayList<>();
        List<String> rollTexts = new ArrayList<>();
        for (SiteSetting info : introSettings) {
            processIntroField(info, introduction, favorites, rollTexts);
        }
        introduction.setFavorites(favorites);
        introduction.setRollText(rollTexts);
        return introduction;
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
        System.out.println(map);
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
    /// Xử lý từ Field trong Introduction
    private void processIntroField(SiteSetting siteSetting, Introduction intro,
                                   List<Favorite> favorites, List<String> rollTexts){
        String nameEn = siteSetting.getNameEn();
        String value = siteSetting.getValue();
        Favorite favorite = null;
        if(nameEn.equals("favorite")){
            try {
                favorite = objectMapper.readValue(value, Favorite.class);
            }catch (JsonProcessingException ex){
                log.warn("Lỗi parse Favorite: {}", value);
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
            case "favorite" -> favorites.add(favorite);
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

    public void updateAll(Map<String, Object> map){
        List<LinkedHashMap> siteSettings = (List<LinkedHashMap>) map.get("settings");
        List<Integer> deleteIds = (List<Integer>) map.get("deleteIds");
        log.info("Các id được xóa {}",deleteIds);
        for(Integer id : deleteIds)
            siteSettingService.deleteSettingById(Long.parseLong( id+""));
        for (LinkedHashMap s : siteSettings){
            SiteSetting siteSetting = objectMapper.convertValue(s,SiteSetting.class);
            if(siteSetting.getId() != null)
                siteSettingService.updateSiteSetting(siteSetting);
            else
                siteSettingService.saveSiteSetting(siteSetting);
        }
    }


}
