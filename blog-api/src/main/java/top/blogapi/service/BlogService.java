package top.blogapi.service;

import com.github.pagehelper.PageInfo;
import top.blogapi.dto.internal.*;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.model.entity.Blog;

import java.util.List;
import java.util.Map;

public interface BlogService {
    PageInfo<Blog> getListByTitleOrCategory(BlogQueryRequest blogQueryRequest);

    void deleteBlogById(Long id);

    void deleteBlogTagByBlogId(Long id);

    void saveBlog(Blog blog);

    void updateBlog(Blog blog);

    Blog getBlogById(Long id);

    void saveBlogTag(Long blogId, List<Long> tagId);

    void updateBlogPublishedById(BlogUpdatePublishedRequest blogUpdatePublishedRequest);

    void updateBlogRecommendById(BlogUpdateRecommendRequest blogUpdateRecommendRequest);

    void countBlogByCategoryId(Long categoryId);

    void updateBlogTopById(Long blogId, Boolean top);

    void countBlogByTagId(Long tagId);

    List<BlogIdAndTitleInternal> getIdAndTitleList();

    List<BlogTagsInfoInternal> getBlogInfoListByIsPublished();

    List<BlogIdAndTitleInternal> getIdAndTitleListByIsPublishedAndIsRecommend();

    List<String> getGroupYearMonthAndIsPublished();

    List<ArchiveBlogInternal> getArchiveBlogListByYearMonthAndIsPublished(List<String> yearMonths);

    BlogDetailInternal getBlogByIdAndIsPublished(Long id);

    Boolean getCommentEnabledByBlogId(Long blogId);

    List<SearchBlog> searchBlogs(String search);

    void flushViewsAllBlogs(Map<Long, Long> map);

    Long getViewsByBlogId(Long blogId);
}
