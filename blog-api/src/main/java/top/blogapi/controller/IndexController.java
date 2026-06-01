package top.blogapi.controller;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.dto.internal.BlogIdAndTitleInternal;
import top.blogapi.dto.internal.VisitDto;
import top.blogapi.dto.response._common.Result;
import top.blogapi.model.entity.Visit;
import top.blogapi.service.GuestService;
import top.blogapi.service.cacheService.VisitCacheService;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;
import top.blogapi.service.impl.orchestration.SiteSettingOrchestrator;
import top.blogapi.util.IpAddressUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static top.blogapi.constant.HeaderConstant.TOKEN_HEADER;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class IndexController {
    BlogOrchestrator blogOrchestrator;
    SiteSettingOrchestrator siteSettingOrchestrator;
    GuestService guestService;
    VisitCacheService visitCacheService;

    @GetMapping("/site")
    public Result<?> site(HttpServletRequest request){
        Map<String, Object> map = siteSettingOrchestrator.getSiteInfo();
        List<BlogIdAndTitleInternal> newBLogList = blogOrchestrator.getIdAndTitleListByIsPublishedAndIsRecommend();
        map.put("newBlogList", newBLogList);
        return Result.ok("Yêu cầu thành công !!", map);
    }
    @GetMapping("/")
    public Result<?> health() {
        System.out.println("oke");
        Queue<VisitDto> visitExpires = visitCacheService.getVisitExpires();
        Cache<Long, VisitDto> cache = visitCacheService.getVisitCache();

        return Result.ok("oke",
                    Map.of(
                           "Đang hoạt động",List.of(cache.asMap().entrySet().toArray()),
                           "Hết hiệu lực", Arrays.asList(visitExpires.toArray()).reversed(),
                           "Stats", cache.stats().toString()
                    )
                );

    }

    @GetMapping("/ip")
    public Map<String,String> getIp(HttpServletRequest request){
        String ip = IpAddressUtils.getIpAddress(request);
        return Map.of(
                "ip",ip
        );
    }

    @GetMapping("/guest/bootstrap")
    public ResponseEntity<Void> bootstrapGuestToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String token = request.getHeader(TOKEN_HEADER);

        if (token == null || token.isBlank()) {

            String guestToken = guestService.createGuestToken();
            System.out.println("TokenGuest: "+ guestToken );
            guestService.getGuestOrCreateByToken(guestToken);
            response.setHeader(
                    TOKEN_HEADER,
                    guestToken
            );

            response.setHeader(
                    HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                    TOKEN_HEADER
            );
        }

        return ResponseEntity.ok().build();
    }
    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String host = request.getHeader("Host");

        System.out.println("Origin: " + origin);
        System.out.println("Referer: " + referer);
        System.out.println("Host: " + host);

        return "ok";
    }
    @GetMapping("/guest")
    public void guest(
            @CookieValue(value = "guest_token", required = false) String guestToken,
            @RequestParam String redirect,
            HttpServletResponse response
    ) throws IOException {

        System.out.println(guestToken);

        if (guestToken == null) {
            guestToken = guestService.createGuestToken();
            guestService.getGuestOrCreateByToken(guestToken);

            ResponseCookie cookie = ResponseCookie.from("guest_token", guestToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(100*366))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        }
        // Redirect về FE
        String url = redirect
                + (redirect.contains("?") ? "&" : "?")
                + "guest=" + guestToken;

        System.out.println(url);

        response.sendRedirect(url);
    }
}