package top.blogapi.content.hashtag.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.content.hashtag.application.query.GetTopHashtagQuery;
import top.blogapi.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final GetTopHashtagQuery getTopHashtagQuery;

    @GetMapping("/top")
    public ResponseEntity<ApiResponse> getTop(@RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(getTopHashtagQuery.execute(limit)));
    }
}
