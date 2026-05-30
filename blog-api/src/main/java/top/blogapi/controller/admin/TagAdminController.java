package top.blogapi.controller.admin;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.tag.CreateTagRequest;
import top.blogapi.dto.request.tag.TagQueryRequest;
import top.blogapi.dto.request.tag.UpdateTagRequest;
import top.blogapi.service.impl.orchestration.TagOrchestrator;
import top.blogapi.dto.response._common.Result;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class TagAdminController {
    TagOrchestrator tagOrchestrator;

    @GetMapping("/tags")
    public Result<?> tags(@ModelAttribute TagQueryRequest request) {
        return Result.ok("yêu cầu thành công!!",tagOrchestrator.getTagListPage(request));
    }

    @PostMapping("/tag")
    public Result<?> createTag(@RequestBody CreateTagRequest request) {
        tagOrchestrator.createTag(request);
        return Result.ok("Tạo Tag thành công !!");
    }

    @PutMapping("/tag")
    public Result<?> updateTag(@RequestBody UpdateTagRequest request){
        tagOrchestrator.updateTag(request);
        return Result.ok("Cập nhật tag thành công!!");
    }

    @DeleteMapping("/tag/{id}")
    public Result<?> deleteTag(@PathVariable Long id){
        tagOrchestrator.deleteTagById(id);
        return Result.ok("Xóa tag thành công!!");
    }
}
