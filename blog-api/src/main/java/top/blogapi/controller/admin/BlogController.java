package top.blogapi.controller.admin;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.dto.response.page.BlogListPageResponse;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Category;
import top.blogapi.entity.Tag;
import top.blogapi.entity.User;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.service.BlogService;
import top.blogapi.service.CategoryService;
import top.blogapi.service.TagService;
import top.blogapi.util.Result;

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
    BlogMapper blogMapper;

    @GetMapping("/blogs")
    public Result<?> blogs(@ModelAttribute BlogQueryRequest blogQueryRequest) {
        System.out.println(blogQueryRequest);
        try(Page<Object> page = PageHelper.startPage(blogQueryRequest.getPageNum(), blogQueryRequest.getPageSize(),
                blogQueryRequest.getSortBy() + " " + blogQueryRequest.getSortOrder());) {

            PageInfo<Blog> pageInfo =
                    new PageInfo<>( blogService.getListByTitleOrCategory(blogQueryRequest.getQuery(),
                            blogQueryRequest.getCategoryId()));

            PageInfo<BlogSummaryResponse> pageInfoDto = pageInfo.convert(blogMapper::toBlogSummaryResponse);

            List<CategoryResponse> categories = categoryService.getCategoryList();
            BlogListPageResponse blogListPageResponse = new BlogListPageResponse(pageInfoDto,categories);

            return Result.ok("Yêu cầu thành công",blogListPageResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/blogs/{id}")
    public Result<?> delete(@PathVariable Long id) {
        try{
            int r = blogService.deleteBlogById(id);
            if(r != 1)
                return Result.error("Không xóa Blog được");
            int r1 = blogService.deleteBlogTagByBlogId(id);
            if(r1 != 1)
                return Result.error("Lỗi không duy trì được bảng liên kết thẻ blog");
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
        System.out.println(map);
        try{
            Map<String, Object> blogMap = (Map<String, Object>) map.get("blog");
            JSONObject blogJsonObject = new JSONObject(blogMap);
            Blog blog = blogJsonObject.toJavaObject(Blog.class);
            System.out.println(blog);
            // Xác minh các thuộc tính
            if (!(StringUtils.hasText(blog.getTitle()) || StringUtils.hasText(blog.getContent())
                    || StringUtils.hasText(blog.getFirstPicture()) || StringUtils.hasText(blog.getDescription())
                    || StringUtils.hasText(blog.getFlag()) || blog.getWords() == null || blog.getWords() < 0)) {
                return Result.error("Tham số không chính xác");
            }

            // Xử lý phân loại
            Object cate = blogMap.get("cate");
            if(cate == null)
                return Result.error("Thể loại không được để trống");

            if(cate instanceof Integer) { // Chọn danh mục hiện có
                Category category = categoryService.getCategoryById(((Integer) cate).longValue());
                if(category != null)
                    blog.setCategory(category);
                else
                    return Result.error("Thể loại không tồn tại");
            }else if(cate instanceof String) {
                Category category = new Category();
                category.setName((String) cate);
                int r = categoryService.saveCategory(category);
                if(r == 1) // Thêm thể loại thành công
                    blog.setCategory(category);
                else // Không thêm thể loại được
                    return Result.error("Thêm thể loại không thành công");

            }else
                return Result.error("Lỗi thêm thể loại");


            // Xử lý tags
            List<Object> tagList = (List<Object>) blogMap.get("tagList");
            List<Tag> tags = new ArrayList<>();
            for(Object t : tagList){
                if(t instanceof Integer){
                    Tag tag = tagService.getTagById(((Integer)t).longValue());
                    if(tag != null)
                        tags.add(tag);
                    else
                        return Result.error("Tag không tồn tại");
                } else if (t instanceof String) {
                    Tag tag = new Tag();
                    tag.setName((String) t);
                    int r = tagService.saveTag(tag);
                    if(r == 1) // Thêm tag thành công
                        tags.add(tag);
                    else
                        Result.error("Thêm tag không thành công");
                }else
                    Result.error("Ghi tên không chính xác");
            }

            User user = new User();
            user.setId(1L);
            blog.setUser(user);
            // Tính toán thời gian đọc với 200từ/phút + 5 phút ngoài(ảnh, code,...)
            blog.setReadTime((int)Math.round(blog.getWords()/200.0) + 5);

            int r = blogService.saveBlog(blog);
            if(r == 1){
                for (Tag t : tags){
                   int r1 = blogService.saveBlogTag(blog.getId(),t.getId());
                   if(r1 != 1)
                       Result.error("Thêm bảng liên kết bị lỗi");
                }
            }else
                Result.error("Thêm blog không thành công");
            Result.ok("Thêm blog thành công");
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
        return null;
    }
}
