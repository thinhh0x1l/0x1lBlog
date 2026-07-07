package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.HashtagOrchestrator;

/**
 * Endpoint để lấy hashtag thịnh hành và hàng đầu.
 */
@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagOrchestrator hashtagOrchestrator;

    @GetMapping("/top")
    public ResponseEntity<ApiResponse> getTop(@RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(hashtagOrchestrator.getTop(limit)));
    }
}
