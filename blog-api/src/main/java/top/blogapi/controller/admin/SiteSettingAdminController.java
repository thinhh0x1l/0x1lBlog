package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.SiteSetting;
import top.blogapi.orchestrator.SiteSettingOrchestrator;

import java.util.List;

/**
 * Endpoint quản trị để lấy và cập nhật cài đặt toàn trang.
 */
@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
public class SiteSettingAdminController {

    private final SiteSettingOrchestrator siteSettingOrchestrator;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(siteSettingOrchestrator.getAll()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAll(@RequestBody List<SiteSetting> settings) {
        siteSettingOrchestrator.updateAll(settings);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
