package top.blogapi.controller.admin;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.siteSetting.SiteSettingUpdateReq;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.dto.response._common.Result;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;
import top.blogapi.service.impl.orchestration.SiteSettingOrchestrator;
import top.blogapi.util.markdown.MarkdownUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class SiteSettingAdminController {
    BlogOrchestrator blogOrchestrator;
    SiteSettingOrchestrator siteSettingOrchestrator;


    @GetMapping("/site-settings")
    public Result<?> getSiteSetting() {
        Map<String, List<SiteSetting>> map = siteSettingOrchestrator.getList();
        return Result.ok("Yêu cầu thành công !!", map);
    }

    @GetMapping("/markdown/1")
    public Result<?> getMarkdown1(@RequestParam String text) {
        return Result.ok("OKe1", MarkdownUtils.markdownToHtmlExtensions(text));
    }
    @GetMapping("/markdown/2")
    public Result<?> getMarkdown2(@RequestParam String text) {
        return Result.ok("OKe1", MarkdownUtils.markdownToHtml(text));
    }

    @PostMapping("/site-settings")
    public Result<?> updateSiteSettingAll(@RequestBody SiteSettingUpdateReq req) {
        siteSettingOrchestrator.updateAll(req);
        return Result.ok("Cập nhật Setting thành công!");
    }
}
