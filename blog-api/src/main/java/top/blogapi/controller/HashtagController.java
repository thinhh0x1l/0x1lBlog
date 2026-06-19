package top.blogapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.model.entity.Hashtag;
import top.blogapi.service.hashtag.HashtagService;

@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping("/top")
    public ResponseEntity<ApiResponse> getTop(@RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(hashtagService.getTopHashtags(limit)));
    }
}
