package top.blogapi.service.impl.orchestration;


import com.alibaba.fastjson2.JSONObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Category;
import top.blogapi.entity.Tag;
import top.blogapi.entity.User;
import top.blogapi.exception.business_exception.domain_exception.BlogServiceException;
import top.blogapi.exception.business_exception.domain_exception.CategoryServiceException;
import top.blogapi.exception.business_exception.domain_exception.TagServiceException;
import top.blogapi.service.BlogService;
import top.blogapi.service.CategoryService;
import top.blogapi.service.TagService;
import top.blogapi.util.Result;
import top.blogapi.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlogOrchestrator {
    BlogService blogService;
    CategoryService categoryService;
    TagService tagService;

    public String getResult(Map<String, Object> map , String type){
        try{
            Map<String, Object> blogMap = (Map<String, Object>) map.get("blog");
            JSONObject blogJsonObject = new JSONObject(blogMap);
            Blog blog = blogJsonObject.toJavaObject(Blog.class);
            System.out.println(blog);
            // Xác minh các thuộc tính
            if(StringUtils.isEmpty(
                    blog.getTitle(),
                    blog.getContent(),
                    blog.getFirstPicture(),
                    blog.getDescription(),
                    blog.getFlag())
                    || blog.getWords() == null || blog.getWords() < 0){
                throw BlogServiceException.builder()
                        .invalidParameters("Các thông tin không được để trống")
                        .build();
            }
            // Xử lý thể loại(category)
            Object cate = blogMap.get("cate");
            if(cate == null)
                throw BlogServiceException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Thể loại không được để trống")
                        .build();


            if(cate instanceof Integer) { // Chọn danh mục hiện có
                Category category = categoryService.getCategoryById(((Integer) cate).longValue());
                blog.setCategory(category);
            }else if(cate instanceof String) {
                categoryService.getCategoryByName((String) cate);
                Category category = new Category();
                category.setName((String) cate);
                int r = categoryService.saveCategory(category);
                if(r == 1) // Thêm thể loại thành công
                    blog.setCategory(category);
                else // Không thêm thể loại được
                    throw CategoryServiceException.builder()
                        .blogDoesntAddCategories("BLOG",HttpStatus.INTERNAL_SERVER_ERROR,"Thêm thể loại không thành công")
                        .build();
            }else
                throw CategoryServiceException.builder()
                        .blogDoesntAddCategories("BLOG",HttpStatus.BAD_REQUEST,"Lỗi thêm thể loại")
                        .build();
            // Xử lý tags
            List<Object> tagList = (List<Object>) blogMap.get("tagList");
            List<Tag> tags = new ArrayList<>();
            for(Object t : tagList){
                if(t instanceof Integer){
                    Tag tag = tagService.getTagById(((Integer)t).longValue());
                    tags.add(tag);
                } else if (t instanceof String) {
                    tagService.getTagByName(t.toString());
                    Tag tag = new Tag();
                    tag.setName((String) t);
                    int r = tagService.saveTag(tag);
                    if(r == 1) // Thêm tag thành công
                        tags.add(tag);
                    else
                        throw TagServiceException.builder()
                                .addTagNotSuccess("BLOG",HttpStatus.INTERNAL_SERVER_ERROR,"Thêm tag không thành công")
                                .build();
                }else
                    throw TagServiceException.builder()
                            .nameTagIncorrect("BLOG",HttpStatus.BAD_REQUEST,"Tên Tag không chính xác")
                            .build();
            }


            if(blog.getReadTime() == null || blog.getReadTime() < 0)
                // Tính toán thời gian đọc với 200từ/phút + 5 phút ngoài(ảnh, code,...)
                blog.setReadTime((int)Math.round(blog.getWords()/200.0) + 5);
            if(blog.getViews() == null || blog.getViews() < 0)
                blog.setViews(0);

            if(type.equals("save")){
                User user = new User();
                user.setId(1L);
                blog.setUser(user);

                int r = blogService.saveBlog(blog);
                if(r == 1 ){// Blog đã được insert thành công
                    // Liên kết Blog và Tags thông qua blog_tag
                    for(Tag tag: tags){
                        int r1 = blogService.saveBlogTag(blog.getId(), tag.getId());
                        if(r1 != 1)
                            throw BlogServiceException.builder()
                                    .blogTagException("BLOG",HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi liên kết bảng")
                                    .build();
                    }
                }
                else
                    throw BlogServiceException.builder()
                            .createBlogFailed("BLOG",HttpStatus.INTERNAL_SERVER_ERROR,"Tạo Blog thất bại !!")
                            .build();

                return "Tạo Blog thành công !!";
            }else{
                int r = blogService.updateBlog(blog);
                if(r == 1){
                    int r1 = blogService.deleteBlogTagByBlogId(blog.getId());
                    if (r1 != 0){
                        for(Tag tag: tags){
                            int r2 = blogService.saveBlogTag(blog.getId(), tag.getId());
                            if (r2 != 1)
                                throw BlogServiceException.builder()
                                    .blogTagException("BLOG",HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi liên kết bảng")
                                    .build();
                        }
                    }else
                        throw BlogServiceException.builder()
                                .createBlogFailed("BLOG",HttpStatus.INTERNAL_SERVER_ERROR,"Cập nhật Blog không thành công !!")
                                .build();
                }
                return "Cập nhật Blog không thành công !!";
            }
        }catch (Exception ignored){
        }
        return null;
    }
}
