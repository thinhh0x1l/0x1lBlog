package top.blogapi.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.blogapi.entity.Blog;
import top.blogapi.service.BlogService;
import top.blogapi.util.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/admin")
public class BlogController {
    BlogService blogService;

    @GetMapping("/blogs")
    public Result blogs() {
        try {
            List<Blog> blogs = blogService.getBlogList();
            Map<String, Object> map = new HashMap<>();
            map.put("blogs", blogs);
            return Result.ok("Yêu cầu thành công", map);
        } catch (Exception e) {
            return Result.create(500, "Lỗi ngoại lệ");
        }
    }
}
