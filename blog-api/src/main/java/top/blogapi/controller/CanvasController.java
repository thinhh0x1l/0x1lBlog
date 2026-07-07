package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.mapper.CanvasMapper;
import top.blogapi.dto.request.canvas.CanvasRequest;
import top.blogapi.dto.response.CanvasResponse;
import top.blogapi.dto.response.CanvasStrokeResponse;
import top.blogapi.orchestrator.CanvasOrchestrator;
import top.blogapi.security.UserPrincipal;

import java.util.List;

/**
 * Quản lý canvas pixel cộng tác: tạo, vẽ nét và đặt lại bởi quản trị viên.
 */
@RestController
@RequestMapping("/api/canvases")
@RequiredArgsConstructor
public class CanvasController {

    private final CanvasOrchestrator canvasOrchestrator;
    private final CanvasMapper canvasMapper;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody CanvasRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                canvasMapper.toResponse(canvasOrchestrator.createCanvas(request, principal.getId()))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                canvasMapper.toResponse(canvasOrchestrator.getCanvas(id))));
    }

    @PostMapping("/{id}/strokes")
    public ResponseEntity<ApiResponse> addStroke(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody CanvasRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                canvasMapper.toStrokeResponse(canvasOrchestrator.addStroke(id, request, principal.getId()))));
    }

    @GetMapping("/{id}/strokes")
    public ResponseEntity<ApiResponse> getStrokes(@PathVariable Long id) {
        List<CanvasStrokeResponse> strokes = canvasOrchestrator.getStrokes(id).stream()
                .map(canvasMapper::toStrokeResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(strokes));
    }

    @PostMapping("/{id}/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> reset(@PathVariable Long id) {
        canvasOrchestrator.resetCanvas(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
