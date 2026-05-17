package top.blogapi.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.model.vo.Result;
import top.blogapi.service.cacheService.BlogCacheService;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BlogController {
    BlogOrchestrator blogOrchestrator;
    BlogCacheService blogViewCacheService;

    @GetMapping("/blog")
    public Result<?> getBlog(@RequestParam Long id){
        blogViewCacheService.increase(id);
        return Result.ok("Yêu cầu thành công", blogOrchestrator.getBlogDetail(id));
    }

    @GetMapping("/search-blog")
    public Result<?> searchBlog(@RequestParam("query") String search){
        return Result.ok("Yêu cầu thành công",blogOrchestrator.searchBlogs(search));
    }
}
