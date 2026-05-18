package top.blogapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.model.entity.Guest;
import top.blogapi.model.vo.BlogIdAndTitle;
import top.blogapi.model.vo.Result;
import top.blogapi.service.GuestService;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;
import top.blogapi.service.impl.orchestration.SiteSettingOrchestrator;
import top.blogapi.util.IpAddressUtils;

import java.util.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class IndexController {
    BlogOrchestrator blogOrchestrator;
    SiteSettingOrchestrator siteSettingOrchestrator;
    GuestService guestService;

    @GetMapping("/site")
    public Result<?> site(HttpServletRequest request){
        Map<String, Object> map = siteSettingOrchestrator.getSiteInfo();
        List<BlogIdAndTitle> newBLogList = blogOrchestrator.getIdAndTitleListByIsPublishedAndIsRecommend();
        map.put("newBlogList", newBLogList);
        String guestToken =  (String)request.getAttribute("guestToken");
        guestService.getGuestOrCreateByToken(guestToken);
        return Result.ok("Yêu cầu thành công !!", map);
    }
    @GetMapping("/")
    public String health() {
        System.out.println("oke");
        return "OK";
    }

    @GetMapping("/ip")
    public Map<String,String> getIp(HttpServletRequest request){
        String ip = IpAddressUtils.getIpAddress(request);
        return Map.of(
                "ip",ip
        );
    }
}