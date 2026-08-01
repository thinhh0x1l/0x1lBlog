package top.blogapi.admin.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.admin.application.query.GetSettingsQuery;
import top.blogapi.admin.application.command.UpdateSettingsCommand;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
public class SiteSettingAdminController {

    public record SettingEntry(
            @NotBlank(message = "Key is required") String key,
            String value,
            String type,
            String description
    ) {}

    private final GetSettingsQuery getSettingsQuery;
    private final UpdateSettingsCommand updateSettingsCommand;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return ResponseEntity.ok(ApiResponse.success(getSettingsQuery.execute()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAll(@Valid @RequestBody List<SettingEntry> settings) {
        updateSettingsCommand.execute(settings);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
