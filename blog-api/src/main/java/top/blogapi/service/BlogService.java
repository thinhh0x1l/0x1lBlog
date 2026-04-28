package top.blogapi.service;

import com.github.pagehelper.PageInfo;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.vo.ArchiveBlog;
import top.blogapi.model.vo.BlogDetail;
import top.blogapi.model.vo.BlogIdAndTitle;
import top.blogapi.model.vo.BlogInfo;

import java.util.List;

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

    List<BlogIdAndTitle> getIdAndTitleList();

    List<BlogInfo> getBlogInfoListByIsPublished();

    List<BlogIdAndTitle> getIdAndTitleListByIsPublishedAndIsRecommend();

    List<String> getGroupYearMonthAndIsPublished();

    List<ArchiveBlog> getArchiveBlogListByYearMonthAndIsPublished(List<String> yearMonths);

    BlogDetail getBlogByIdAndIsPublished(Long id);

    Boolean getCommentEnabledByBlogId(Long blogId);

    void updateViewByBlogId(Long id);
}
