package top.blogapi.social.mischief.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.social.mischief.application.query.GetMischiefQuery;

@RestController
@RequestMapping("/api/mischief")
@RequiredArgsConstructor
public class MischiefController {

    private final GetMischiefQuery getMischiefQuery;

    @GetMapping("/badges")
    public ResponseEntity<ApiResponse> getAllBadges() {
        return ResponseEntity.ok(ApiResponse.success(getMischiefQuery.getAllBadges()));
    }

    @GetMapping("/users/{userId}/badges")
    public ResponseEntity<ApiResponse> getUserBadges(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(getMischiefQuery.getUserBadges(userId)));
    }
}
