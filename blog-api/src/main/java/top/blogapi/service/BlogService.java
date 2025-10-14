package top.blogapi.service;

import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getListByTitleOrCategory(String query, Integer categoryId);

    int deleteBlogById(Long id);

    int deleteBlogTagByBlogId(Long id);

    int saveBlog(Blog blog);

    int saveBlogTag(Long blogId, Long tagId);
}
