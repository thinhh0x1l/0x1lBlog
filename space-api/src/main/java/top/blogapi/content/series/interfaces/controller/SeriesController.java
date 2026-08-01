package top.blogapi.content.series.interfaces.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.content.series.application.command.CreateSeriesCommand;
import top.blogapi.content.series.application.command.UpdateSeriesCommand;
import top.blogapi.content.series.application.command.DeleteSeriesCommand;
import top.blogapi.content.series.application.command.AddBlogToSeriesCommand;
import top.blogapi.content.series.application.command.RemoveBlogFromSeriesCommand;
import top.blogapi.content.series.application.query.GetSeriesQuery;
import top.blogapi.infra.security.UserPrincipal;
import top.blogapi.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    public record CreateSeriesRequest(
            @NotBlank(message = "Name is required") @Size(max = 200) String name,
            @Size(max = 1000) String description,
            @Size(max = 500) String coverImage
    ) {}

    public record UpdateSeriesRequest(
            @Size(max = 200) String name,
            @Size(max = 1000) String description,
            @Size(max = 500) String coverImage
    ) {}

    private final GetSeriesQuery getSeriesQuery;
    private final CreateSeriesCommand createSeriesCommand;
    private final UpdateSeriesCommand updateSeriesCommand;
    private final DeleteSeriesCommand deleteSeriesCommand;
    private final AddBlogToSeriesCommand addBlogToSeriesCommand;
    private final RemoveBlogFromSeriesCommand removeBlogFromSeriesCommand;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(getSeriesQuery.execute(id)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse> getByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(getSeriesQuery.getByAuthor(authorId, page, size)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @RequestBody CreateSeriesRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(createSeriesCommand.execute(request, principal.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id,
                                              @Valid @RequestBody UpdateSeriesRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(updateSeriesCommand.execute(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@AuthenticationPrincipal UserPrincipal principal,
                                              @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        deleteSeriesCommand.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{seriesId}/blogs/{blogId}")
    public ResponseEntity<ApiResponse> addBlog(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long seriesId,
                                               @PathVariable Long blogId,
                                               @RequestParam(defaultValue = "0") int sortOrder) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        addBlogToSeriesCommand.execute(seriesId, blogId, sortOrder);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{seriesId}/blogs/{blogId}")
    public ResponseEntity<ApiResponse> removeBlog(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long seriesId,
                                                  @PathVariable Long blogId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        removeBlogFromSeriesCommand.execute(seriesId, blogId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
