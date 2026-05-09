package top.blogapi.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.model.entity.Tag;
import top.blogapi.service.BlogService;
import top.blogapi.service.TagService;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;
import top.blogapi.service.impl.orchestration.CategoryOrchestrator;
import top.blogapi.model.vo.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class BlogAdminController {
    BlogService blogService;
    TagService tagService;
    CategoryOrchestrator categoryOrchestrator;
    BlogOrchestrator blogOrchestrator;
    @GetMapping("/blogs")
    public Result<?> blogs(@ModelAttribute BlogQueryRequest blogQueryRequest) {
        return Result.ok("Yêu cầu thành công",blogOrchestrator.getListByTitleOrCategory(blogQueryRequest));
    }

    @GetMapping("/blog/{id}")
    public Result<?> getBlog(@PathVariable Long id) {
        return Result.ok("Lấy Blog thành công",blogService.getBlogById(id));
    }

    @DeleteMapping("/blog/{id}")
    public Result<?> delete(@PathVariable Long id) {
        blogOrchestrator.deleteBlogById(id);
        return Result.ok("Xóa blog thành công");

    }

    @PutMapping("/blog/top")
    public Result<?> updateTop(@RequestParam Long id, @RequestParam Boolean top) {
        blogOrchestrator.updateBlogTopById(id, top);
        return Result.ok("Cập nhật ghim blog thành công !!");
    }

    @GetMapping("/categoryAndTag")
    public Result<?> categoryAndTag() {
        try{
            List<CategoryResponse> categories = categoryOrchestrator.getCategoryResponsesList();
            List<Tag> tags = tagService.getTagList();
            Map<String,Object> map = new HashMap<>();
            map.put("categories", categories);
            map.put("tags", tags);
            return Result.ok("Yêu cầu thành công",map);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @PostMapping("/blog")
    public Result<?> saveBlog(@RequestBody Map<String, Object> map) {
        return Result.ok(blogOrchestrator.getResult(map,"save"));
    }

    @PutMapping("/blog")
    public Result<?> updateBlog(@RequestBody Map<String, Object> map) {
        return Result.ok(blogOrchestrator.getResult(map,"update"));
    }


    @PutMapping("/blog/recommend")
    public Result<?> updateBlogRecommend(@RequestBody BlogUpdateRecommendRequest blogUpdateRecommendRequest) {
        blogService.updateBlogRecommendById(blogUpdateRecommendRequest);
        return Result.ok("Cập nhật blog thành công");
    }

    @PutMapping("/blog/published")
    public Result<?> updateBlogPublished(@RequestBody BlogUpdatePublishedRequest blogUpdatePublishedRequest) {
        blogService.updateBlogPublishedById(blogUpdatePublishedRequest);
        return Result.ok("Cập nhật thành công");
    }

    @GetMapping("/blogIdAndTitle")
    public Result<?> getBlogIdAndTitle() {
        return Result.ok("getBlogIdAndTitle",blogOrchestrator.getIdAndTitleList());
    }
}
