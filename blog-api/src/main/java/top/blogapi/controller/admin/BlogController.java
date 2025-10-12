package top.blogapi.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Category;
import top.blogapi.service.BlogService;
import top.blogapi.service.CategoryService;
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
    CategoryService categoryService;

    @GetMapping("/blogs")
    public Result blogs(@RequestParam(defaultValue = "") String query,
                        @RequestParam(required = false) Integer typeId,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize) {
        String orderBy = "create_time desc";
        try(Page<Object> page = PageHelper.startPage(pageNum, pageSize, orderBy)) {
            PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getListByTitleOrType(query, typeId));
            List<Category> categories = categoryService.getCategoryList();
            Map<String, Object> map = new HashMap<>();
            map.put("blogs", pageInfo);
            map.put("categories", categories);
            return Result.ok("Yêu cầu thành công", map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create(500, "Lỗi ngoại lệ: " + e.getMessage());
        }
    }

    @DeleteMapping("/blogs/{id}")
    public Result delete(@PathVariable Long id) {
        try{
            blogService.deleteBlogById(id);
            return Result.ok("Xóa thành công");
        }catch (Exception e) {
            e.printStackTrace();
            return Result.create(500, "[Delete] - Lỗi ngoại lệ: " + e.getMessage());
        }
    }
}
