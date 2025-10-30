package top.blogapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.request.blog.BlogUpdatePublishedRequest;
import top.blogapi.dto.request.blog.BlogUpdateRecommendRequest;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.dto.response.page.BlogListPageResponse;
import top.blogapi.entity.Blog;
import top.blogapi.entity.Tag;
import top.blogapi.exception.business_exception.domain_exception.BlogServiceException;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.mapper.CategoryMapper;
import top.blogapi.repository.BlogRepository;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.BlogService;
import top.blogapi.util.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class BlogServiceImpl implements BlogService {
    BlogRepository blogRepository;
    BlogMapper blogMapper;
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public BlogListPageResponse getListByTitleOrCategory(BlogQueryRequest blogQueryRequest) {
        validateBlogQuery(blogQueryRequest);
        try(Page<Object> page = PageHelper.startPage(
                blogQueryRequest.getPageNum()
                , blogQueryRequest.getPageSize(),
                blogQueryRequest.getSortBy() + " " + blogQueryRequest.getSortOrder())) {
            PageInfo<Blog> pageInfo = new PageInfo<>(
                    blogRepository.getListByTitleOrCategory(
                            blogQueryRequest.getQuery(),
                            blogQueryRequest.getCategoryId()
                    )
            );
            PageInfo<BlogSummaryResponse> pageInfoResponse =
                    pageInfo.convert(blogMapper::toBlogSummaryResponse);
            return new BlogListPageResponse(pageInfoResponse, retrieveCategories());
        }catch (DataAccessException e){
            throw BlogServiceException.builder()
                    .dataRetrievalFailed("getListByTitleOrCategory")
                    .cause(e.getCause())
                    .context("query", blogQueryRequest.getQuery())
                    .context("categoryId", blogQueryRequest.getCategoryId())
                    .context("pageNum", blogQueryRequest.getPageNum())
                    .build();
        }
    }

    private void validateBlogQuery(BlogQueryRequest request) {
        if(request.getPageNum() <= 0)
            throw BlogServiceException.builder()
                    .errorCode("BLOG_INVALID_PAGE")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Không thể chọn trang nhỏ hơn 1")
                    .context("providedPage", request.getPageNum())
                    .build();
        if(request.getQuery() != null && request.getQuery().length() > 100)
            throw BlogServiceException.builder()
                    .invalidQuery(request.getQuery(),"Vướt quá độ dài tìm kiếm")
                    .context("length",100)
                    .context("actualLength",request.getQuery().length())
                    .build();
    }
    private List<CategoryResponse> retrieveCategories(){
        try{
            return categoryRepository.getCategoryList().stream()
                    .map(categoryMapper::toCategoryResponse).toList();
        } catch (Exception e) {
            log.warn("Failed to retrieve categories, returning empty list", e);
            return List.of();
        }
    }

    @Transactional
    @Override
    public int deleteBlogById(Long id) {
        return blogRepository.deleteBlogById(id);
    }

    @Transactional
    @Override
    public int deleteBlogTagByBlogId(Long id) {
        return blogRepository.deleteBlogTagByBlogId(id);
    }

    @Transactional
    @Override
    public int saveBlog(Blog blog) {
        LocalDateTime now = LocalDateTime.now();
        blog.setCreateTime(now);
        blog.setUpdateTime(now);
        blog.setViews(0);
        return blogRepository.saveBlog(blog);
    }

    @Transactional
    @Override
    public int updateBlog(Blog blog) {
        blog.setUpdateTime(LocalDateTime.now());
        return blogRepository.updateBlog(blog);
    }

    @Transactional
    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogRepository.getBlogById(id)
                .orElseThrow(() -> BlogServiceException.builder()
                        .blogNotFound(id.toString())
                        .build());
        List<Tag> tags = blogRepository.findTagsByBlogId(id);
        blog.setTags(tags);
        return blog;
    }

    @Transactional
    @Override
    public int saveBlogTag(Long blogId, Long tagId) {
        return blogRepository.saveBlogTag(blogId, tagId);
    }

    @Transactional
    @Override
    public void updateBlogPublishedById(BlogUpdatePublishedRequest blogUpdatePublishedRequest) {
         int r = blogRepository.updateBlogPublishedById(blogUpdatePublishedRequest.getId(),
                 blogUpdatePublishedRequest.isPublished());
         if(r != 1)
             throw BlogServiceException.builder()
                     .dataRetrievalFailed("updateBlogRecommendById")
                     .build();
    }

    @Transactional
    @Override
    public void updateBlogRecommendById(BlogUpdateRecommendRequest blogUpdateRecommendRequest) {
        int r  = blogRepository.updateBlogRecommendById(blogUpdateRecommendRequest.getId(), blogUpdateRecommendRequest.isRecommend());
        if(r != 1)
            throw BlogServiceException.builder()
                    .dataRetrievalFailed("updateBlogRecommendById")
                    .build();
    }

    @Override
    public int countBlogByCategoryId(Long categoryId) {
        return 0;
    }

}
