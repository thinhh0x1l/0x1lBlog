package top.blogapi.controller.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Category;
import top.blogapi.entity.Tag;
import top.blogapi.entity.User;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.service.BlogService;
import top.blogapi.service.CategoryService;
import top.blogapi.service.TagService;
import top.blogapi.service.impl.orchestration.BlogOrchestrator;
import top.blogapi.util.Result;
import top.blogapi.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class BlogController {
    BlogService blogService;
    CategoryService categoryService;
    TagService tagService;
    BlogOrchestrator blogOrchestrator;
    @GetMapping("/blogs")
    public Result<?> blogs(@ModelAttribute BlogQueryRequest blogQueryRequest) {
        System.out.println(blogQueryRequest);
        return Result.ok("Yêu cầu thành công",blogService.getListByTitleOrCategory(blogQueryRequest));
    }

    @GetMapping("/blog/{id}")
    public Result<?> getBlog(@PathVariable Long id) {
        return Result.ok("Lấy Blog thành công",blogService.getBlogById(id));
    }

    @DeleteMapping("/blog/{id}")
    public Result<?> delete(@PathVariable Long id) {
        try{
            int r1 = blogService.deleteBlogTagByBlogId(id);
            if(r1 != 1)
                return Result.error("Lỗi không duy trì được bảng liên kết thẻ blog");
            int r = blogService.deleteBlogById(id);
            if(r != 1)
                return Result.error("Không xóa Blog được");

            return Result.ok("Xóa Blog thành công");
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @GetMapping("/categoryAndTag")
    public Result<?> categoryAndTag() {
        try{
            List<CategoryResponse> categories = categoryService.getCategoryList();
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
}
