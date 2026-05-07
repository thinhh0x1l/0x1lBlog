package top.blogapi.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.Result;
import top.blogapi.service.impl.orchestration.TagOrchestrator;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class TagController {
    TagOrchestrator tagOrchestrator;

    @GetMapping("/tags")
    public Result<?> tags(){
        return Result.ok("Yêu cầu thành công", tagOrchestrator.getTagSlugList());
    }

    @GetMapping("/tag/{slug}")
    public Result<?> tag(@PathVariable String slug,
                      @RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.ok("Yêu cầu thành công",
                tagOrchestrator.tagIdGetBlogsResponse(slug,pageNum,pageSize));
    }
}
