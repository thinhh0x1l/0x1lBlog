package top.blogapi.social.canvas.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.social.canvas.interfaces.dto.CanvasMapper;
import top.blogapi.social.canvas.interfaces.dto.CanvasRequest;
import top.blogapi.social.canvas.interfaces.dto.CanvasResponse;
import top.blogapi.social.canvas.interfaces.dto.CanvasStrokeResponse;
import top.blogapi.social.canvas.application.command.CreateCanvasCommand;
import top.blogapi.social.canvas.application.command.AddStrokeCommand;
import top.blogapi.social.canvas.application.query.GetCanvasQuery;
import top.blogapi.infra.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/canvases")
@RequiredArgsConstructor
public class CanvasController {

    private final CreateCanvasCommand createCanvasCommand;
    private final AddStrokeCommand addStrokeCommand;
    private final GetCanvasQuery getCanvasQuery;
    private final CanvasMapper canvasMapper;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody CanvasRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                canvasMapper.toResponse(createCanvasCommand.execute(request, principal.getId()))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                canvasMapper.toResponse(getCanvasQuery.execute(id))));
    }

    @PostMapping("/{id}/strokes")
    public ResponseEntity<ApiResponse> addStroke(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody CanvasRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                canvasMapper.toStrokeResponse(addStrokeCommand.execute(id, request, principal.getId()))));
    }

    @GetMapping("/{id}/strokes")
    public ResponseEntity<ApiResponse> getStrokes(@PathVariable Long id) {
        List<CanvasStrokeResponse> strokes = getCanvasQuery.getStrokes(id).stream()
                .map(canvasMapper::toStrokeResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(strokes));
    }

    @PostMapping("/{id}/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> reset(@PathVariable Long id) {
        getCanvasQuery.resetCanvas(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
