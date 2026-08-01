package top.blogapi.discovery.interfaces.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.discovery.application.query.GetDiscoveryQuery;

@RestController
@RequestMapping("/api/discovery")
@RequiredArgsConstructor
public class DiscoveryController {

    private final GetDiscoveryQuery getDiscoveryQuery;

    @GetMapping("/random")
    public ResponseEntity<ApiResponse> discover(@RequestParam(defaultValue = "blog") String category) {
        return ResponseEntity.ok(ApiResponse.success(getDiscoveryQuery.discover(category)));
    }

    @GetMapping("/random/blog")
    public ResponseEntity<ApiResponse> randomBlog() {
        return ResponseEntity.ok(ApiResponse.success(getDiscoveryQuery.getRandomBlog().orElse(null)));
    }

    @GetMapping("/random/user")
    public ResponseEntity<ApiResponse> randomUser() {
        return ResponseEntity.ok(ApiResponse.success(getDiscoveryQuery.getRandomUser().orElse(null)));
    }
}
