package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.MischiefOrchestrator;

/**
 * Endpoint để duyệt huy hiệu tinh nghịch và bộ sưu tập huy hiệu của người dùng.
 */
@RestController
@RequestMapping("/api/mischief")
@RequiredArgsConstructor
public class MischiefController {

    private final MischiefOrchestrator mischiefOrchestrator;

    @GetMapping("/badges")
    public ResponseEntity<ApiResponse> getAllBadges() {
        return ResponseEntity.ok(ApiResponse.success(mischiefOrchestrator.getAllBadges()));
    }

    @GetMapping("/users/{userId}/badges")
    public ResponseEntity<ApiResponse> getUserBadges(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(mischiefOrchestrator.getUserBadges(userId)));
    }
}
