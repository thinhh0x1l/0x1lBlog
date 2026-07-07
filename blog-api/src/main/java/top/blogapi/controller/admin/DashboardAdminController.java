package top.blogapi.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.orchestrator.DashboardOrchestrator;
import top.blogapi.orchestrator.UserAdminOrchestrator;

/**
 * Bảng điều khiển quản trị: thống kê trang web, danh sách người dùng, cập nhật vai trò và cấm người dùng.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class DashboardAdminController {

    private final DashboardOrchestrator dashboardOrchestrator;
    private final UserAdminOrchestrator userAdminOrchestrator;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardOrchestrator.getStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(userAdminOrchestrator.findAll(page, size)));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @RequestParam String role) {
        userAdminOrchestrator.updateRole(id, role);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<ApiResponse> banUser(@PathVariable Long id) {
        userAdminOrchestrator.banUser(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
