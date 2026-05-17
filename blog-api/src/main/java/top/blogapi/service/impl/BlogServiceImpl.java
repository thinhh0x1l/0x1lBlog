package top.blogapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.*;
import top.blogapi.repository.BlogRepository;
import top.blogapi.service.BlogService;
import top.blogapi.util.markdown.MarkdownUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class BlogServiceImpl implements BlogService {
    BlogRepository blogRepository;

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Blog> getListByTitleOrCategory(BlogQueryRequest blogQueryRequest) {
        try(Page<Object> page = PageHelper.startPage(
                blogQueryRequest.getPageNum()
                , blogQueryRequest.getPageSize(),
                blogQueryRequest.getSortBy() + " " + blogQueryRequest.getSortOrder())) {
            return new PageInfo<>(
                    blogRepository.getListByTitleOrCategory(
                            blogQueryRequest.getQuery(),
                            blogQueryRequest.getCategoryId()
                    )
            );
        }
    }


    @Transactional
    @Override
    public void deleteBlogById(Long id) {
        if(blogRepository.deleteBlogById(id) == 0)
            throw new AppException(ErrorCode.BLOG_NOT_FOUND,"Blog không tồn tại");

    }

    @Transactional
    @Override
    public void deleteBlogTagByBlogId(Long id) {
        int r = blogRepository.deleteBlogTagByBlogId(id);
        if(r == 0)
            throw new AppException(ErrorCode.INTERNAL_ERROR,"Lỗi liên kết Tag-Blog");
    }

    @Transactional
    @Override
    public void saveBlog(Blog blog) {
        LocalDateTime now = LocalDateTime.now();
        blog.setCreateTime(now);
        blog.setUpdateTime(now);
        blog.setViews(0);
        if(blogRepository.saveBlog(blog)==0)
            throw new AppException(ErrorCode.INTERNAL_ERROR,"Lỗi liên kết Tag-Blog");
    }

    @Transactional
    @Override
    public void updateBlog(Blog blog) {
        blog.setUpdateTime(LocalDateTime.now());
        blogRepository.updateBlog(blog);
    }

    @Transactional(readOnly = true)
    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogRepository.getBlogById(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.BLOG_NOT_FOUND,"Blog này không tồn tại"));
        List<Tag> tags = blogRepository.findTagsByBlogId(id);
        blog.setTags(tags);
        return blog;
    }

    @Transactional
    @Override
    public void saveBlogTag(Long blogId, List<Long> tagIds) {
         int r = blogRepository.saveBlogTag(blogId, tagIds);
         if(r == 0)
             throw new AppException(ErrorCode.INTERNAL_ERROR,"Lỗi liên kết bảng");
    }

    @Transactional
    @Override
    public void updateBlogPublishedById(BlogUpdatePublishedRequest blogUpdatePublishedRequest) {
        if(blogRepository.updateBlogPublishedById(blogUpdatePublishedRequest.getId(),
                blogUpdatePublishedRequest.isPublished()) ==0)
            throw new AppException(ErrorCode.BLOG_NOT_FOUND,"Thao tác thất bại");
    }

    @Transactional
    @Override
    public void updateBlogRecommendById(BlogUpdateRecommendRequest blogUpdateRecommendRequest) {
        if(blogRepository.updateBlogRecommendById(blogUpdateRecommendRequest.getId(),
                blogUpdateRecommendRequest.isRecommend())==0)
            throw new AppException(ErrorCode.BLOG_NOT_FOUND,"Thao tác thất bại");
    }

    @Transactional(readOnly = true)
    @Override
    public void countBlogByCategoryId(Long categoryId) {
         blogRepository.countBlogByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public void updateBlogTopById(Long blogId, Boolean top) {
        if(blogRepository.updateBlogTopById(blogId, top)==0)
            throw new AppException(ErrorCode.BLOG_NOT_FOUND,"Blog không tồn tại");
    }

    @Transactional(readOnly = true)
    @Override
    public void countBlogByTagId(Long tagId) {
        blogRepository.countBlogByTagId(tagId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BlogIdAndTitle> getIdAndTitleList() {
        return blogRepository.getIdAndTitleList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BlogTagsInfo> getBlogInfoListByIsPublished() {
        return blogRepository.getBlogInfoListByIsPublished();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BlogIdAndTitle> getIdAndTitleListByIsPublishedAndIsRecommend() {
        return blogRepository.getIdAndTitleListByIsPublishedAndIsRecommend();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getGroupYearMonthAndIsPublished() {
        return blogRepository.getGroupYearMonthAndIsPublished();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArchiveBlog> getArchiveBlogListByYearMonthAndIsPublished(List<String> yearMonths) {
        return blogRepository.getArchiveBlogListByYearMonthAndIsPublished(yearMonths);
    }

    @Transactional(readOnly = true)
    @Override
    public BlogDetail getBlogByIdAndIsPublished(Long id) {
        BlogDetail blogDetail =  blogRepository.getBlogWithCategory(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND,"Blog không tồn tại"));
        blogDetail.setTags(blogRepository.findTagsByBlogId(id));
        blogDetail.setContent(MarkdownUtils.markdownToHtmlExtensions(blogDetail.getContent()));
        return blogDetail;
    }

    @Override
    public Boolean getCommentEnabledByBlogId(Long blogId) {
        return blogRepository.getCommentEnabledByBlogId(blogId);
    }


    @Override
    public List<SearchBlog> searchBlogs(String search) {
        return blogRepository.searchBlogs(search);
    }

    @Override
    public void flushViewsAllBlogs(Map<Long, Long> map) {
        int r = blogRepository.flushViews(map);
        if(map.size() != r)
            log.info("update bị thiếu {}-{}={}",map.size(),r, map.size()-r);
    }


}
