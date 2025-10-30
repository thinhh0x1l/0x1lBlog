package top.blogapi.service;

import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.page.BlogListPageResponse;
import top.blogapi.entity.Blog;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    BlogListPageResponse getListByTitleOrCategory(BlogQueryRequest blogQueryRequest);

    int deleteBlogById(Long id);

    int deleteBlogTagByBlogId(Long id);

    int saveBlog(Blog blog);

    int updateBlog(Blog blog);

    Blog getBlogById(Long id);

    int saveBlogTag(Long blogId, Long tagId);

    void updateBlogPublishedById(BlogUpdatePublishedRequest blogUpdatePublishedRequest);

    void updateBlogRecommendById(BlogUpdateRecommendRequest blogUpdateRecommendRequest);

    int countBlogByCategoryId(Long categoryId);
}
