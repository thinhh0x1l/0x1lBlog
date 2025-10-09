package top.blogapi.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Result blogs(@RequestParam(defaultValue = "") String query,
                        @RequestParam(defaultValue = "") String typeId,
                        @RequestParam(defaultValue = "1") String pageNum,
                        @RequestParam(defaultValue = "10") String pageSize) {
        String orderBy = "create_time desc";
        try(Page<Object> page = PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize), orderBy)) {
            PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getBlogList());
            Map<String, Object> map = new HashMap<>();
            map.put("blogs", pageInfo);
            return Result.ok("Yêu cầu thành công", map);
        } catch (Exception e) {
            return Result.create(500, "Lỗi ngoại lệ");
        }
    }
}
