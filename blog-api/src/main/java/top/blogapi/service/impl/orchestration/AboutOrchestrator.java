package top.blogapi.service.impl.orchestration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.client.zing_mp3.MusicService;
import top.blogapi.config.CacheKeyConfig;
import top.blogapi.dto.response.about.AboutResponse;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.About;
import top.blogapi.service.AboutService;
import top.blogapi.util.markdown.MarkdownUtils;

import java.util.*;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AboutOrchestrator {
    AboutService aboutService;

    MusicService musicService;

    @Cacheable(value = CacheKeyConfig.ABOUT_INFO_MAP)
    public AboutResponse getAboutInfo(){
        AboutResponse aboutResponse = new AboutResponse();
        for (About about : aboutService.getAboutInfo()) {
            String value = about.getValue();
            if ("content".equals(about.getNameEn()))
                value = MarkdownUtils.markdownToHtmlExtensions(value);
            aboutResponse.setter(about.getNameEn(),value);
        }
        aboutResponse.setMusicInfo(musicService.getCompleteSongData(aboutResponse.getMusicId(),2));
        return aboutResponse;
    }

    public List<About> aboutSettings(){
        return aboutService.getAboutInfo();
    }

    public void updateAbouts(List<About> abouts){
        if(aboutService.updateAbout(abouts) == 0)
            throw new AppException(ErrorCode.ABOUT_UPDATE_PARTIAL);
    }
}
