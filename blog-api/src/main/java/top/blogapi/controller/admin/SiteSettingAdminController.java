package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.service.site.SiteSettingService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
public class SiteSettingAdminController {

    private final SiteSettingService siteSettingService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(siteSettingService.findAll()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAll(@RequestBody List<SiteSetting> settings) {
        settings.forEach(siteSettingService::upsert);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
