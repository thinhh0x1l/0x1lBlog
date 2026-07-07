package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.DiscoveryOrchestrator;

/**
 * Cung cấp endpoint khám phá nội dung ngẫu nhiên cho blog và người dùng.
 */
@RestController
@RequestMapping("/api/discovery")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryOrchestrator discoveryOrchestrator;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse> discover(@RequestParam(defaultValue = "blog") String category) {
        return ResponseEntity.ok(ApiResponse.success(discoveryOrchestrator.discover(category)));
    }

    @GetMapping("/random/blog")
    public ResponseEntity<ApiResponse> randomBlog() {
        return ResponseEntity.ok(ApiResponse.success(discoveryOrchestrator.getRandomBlog().orElse(null)));
    }

    @GetMapping("/random/user")
    public ResponseEntity<ApiResponse> randomUser() {
        return ResponseEntity.ok(ApiResponse.success(discoveryOrchestrator.getRandomUser().orElse(null)));
    }
}
