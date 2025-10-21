package top.blogapi.service;

import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.page.BlogListPageResponse;
import top.blogapi.entity.Blog;

import java.util.List;

public interface BlogService {
    BlogListPageResponse getListByTitleOrCategory(BlogQueryRequest blogQueryRequest);

    int deleteBlogById(Long id);

    int deleteBlogTagByBlogId(Long id);

    int saveBlog(Blog blog);

    int saveBlogTag(Long blogId, Long tagId);

    void updateBlogPublishedById(BlogUpdatePublishedRequest blogUpdatePublishedRequest);

    void updateBlogRecommendById(BlogUpdateRecommendRequest blogUpdateRecommendRequest);
}
