package top.blogapi.service.impl.orchestration;


import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.client.zing_mp3.Mp3Service;
import top.blogapi.client.zing_mp3.MusicService;
import top.blogapi.dto.request.blog.BlogQueryRequest;
import top.blogapi.dto.response.blog.ArchiveBlogResponse;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.dto.response._page.BlogListPageResponse;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.*;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.mapper.CategoryMapper;
import top.blogapi.model.vo.ArchiveBlog;
import top.blogapi.model.vo.BlogDetail;
import top.blogapi.model.vo.BlogInfo;
import top.blogapi.service.BlogService;
import top.blogapi.service.CategoryService;
import top.blogapi.service.SiteSettingService;
import top.blogapi.service.TagService;
import top.blogapi.util.StringUtils;
import top.blogapi.model.vo.BlogIdAndTitle;
import top.blogapi.util.markdown.MarkdownUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlogOrchestrator {
    BlogService blogService;
    CategoryService categoryService;
    TagService tagService;
    MusicService musicService;
    BlogMapper blogMapper;
    CategoryMapper categoryMapper;
    SiteSettingOrchestrator siteSettingOrchestrator;

    public BlogListPageResponse getListByTitleOrCategory(BlogQueryRequest blogQueryRequest) {
        validateBlogQuery(blogQueryRequest);

        PageInfo<BlogSummaryResponse> pageInfoResponse =
                blogService.getListByTitleOrCategory(blogQueryRequest).convert(blogMapper::toBlogSummaryResponse);

        List<CategoryResponse> categoryResponses =
                categoryService.getCategoryList().stream().map(categoryMapper::toCategoryResponse).toList();

        return new BlogListPageResponse(pageInfoResponse, categoryResponses);

    }

    private void validateBlogQuery(BlogQueryRequest request) {
        if (request.getPageNum() <= 0)
            throw new AppException(ErrorCode.INVALID_INPUT,"Không thể chọn trang nhỏ hơn 1");
        if (request.getQuery() != null && request.getQuery().length() > 100)
            throw new AppException(ErrorCode.INVALID_INPUT,"Độ dài vượt quá yêu cầu");
    }

    public String getResult(Map<String, Object> map, String type) {
        Map<String, Object> blogMap = (Map<String, Object>) map.get("blog");
        JSONObject blogJsonObject = new JSONObject(blogMap);
        Blog blog = blogJsonObject.toJavaObject(Blog.class);
        System.out.println(blog);
        // Xác minh các thuộc tính
        if (StringUtils.isEmpty(
                blog.getTitle(),
                blog.getContent(),
                blog.getDescription()
//                    , blog.getFlag()
        )
                || blog.getWords() == null || blog.getWords() < 0) {
            throw new AppException(ErrorCode.INVALID_INPUT,"Các thông tin không được để trống");
        }
        // Xử lý thể loại(category)
        Object cate = blogMap.get("cate");
        if (cate == null)
            throw new AppException(ErrorCode.INVALID_INPUT,"Thể loại không được để trống");

        if (cate instanceof Integer) { // Chọn danh mục hiện có
            Category category = categoryService.getCategoryById(((Integer) cate).longValue());
            blog.setCategory(category);
        } else if (cate instanceof String) {
            if (!categoryService.categoryExist((String) cate)) {
                Category category = categoryService.saveCategory((String) cate);
                blog.setCategory(category); // Thêm thể loại thành công
            }
        } else
            throw new AppException(ErrorCode.INTERNAL_ERROR,"Lỗi thêm thể loại");
        // Xử lý tags
        List<Object> tagList = (List<Object>) blogMap.get("tagList");
        List<Tag> tags = new ArrayList<>();

        for (Object t : tagList) {
            if (t instanceof Integer) {
                Tag tag = tagService.getTagById(((Integer) t).longValue());
                tags.add(tag);
            } else if (t instanceof String) {
                if (!tagService.tagExist(t.toString())) {
                    Tag tag = tagService.saveTag((String) t, "black");
                    System.out.println(tag);
                    if (tag.getId() != null) // Thêm tag thành công
                        tags.add(tag);
                    else
                        throw new AppException(ErrorCode.INTERNAL_ERROR,"Thêm tag không thành công");
                }
            } else
                throw new AppException(ErrorCode.INVALID_INPUT,"Tên Tag không chính xác");
        }

        if (blog.getReadTime() == null || blog.getReadTime() < 0)
            // Tính toán thời gian đọc với 200từ/phút + 5 phút ngoài(ảnh, code,...)
            blog.setReadTime((int) Math.round(blog.getWords() / 200.0) + 5);
        if (blog.getViews() == null || blog.getViews() < 0)
            blog.setViews(0);
        List<Long> tagIds = tags.stream().map(Tag::getId).toList();
        if (type.equals("save")) {
            User user = new User();
            user.setId(1L);
            blog.setUser(user);
            blogService.saveBlog(blog);
            blogService.saveBlogTag(blog.getId(), tagIds);
            return "Tạo Blog thành công !!";
        } else {
            blogService.updateBlog(blog);
            blogService.deleteBlogTagByBlogId(blog.getId());
            blogService.saveBlogTag(blog.getId(), tagIds);
        }
        return "Cập nhật Blog thành công !!";
    }
    public void deleteBlogById(Long id){
        blogService.deleteBlogTagByBlogId(id);
        blogService.deleteBlogById(id);
    }
    public List<BlogIdAndTitle> getIdAndTitleList() {
        return blogService.getIdAndTitleList();
    }

    public List<BlogInfo> getBlogInfoListByIsPublished(){
        List<BlogInfo> blogInfos = blogService.getBlogInfoListByIsPublished();
        blogInfos.forEach(blogInfo -> {
            blogInfo.setTags(tagService.getTagListByBlogId(blogInfo.getId()));
            blogInfo.setDescription(MarkdownUtils.markdownToHtmlExtensions(blogInfo.getDescription()));
        });
        return blogInfos;
    }

    public void updateBlogTopById(Long blogId, Boolean top){
        blogService.updateBlogTopById(blogId, top);
    }

    public List<BlogIdAndTitle> getIdAndTitleListByIsPublishedAndIsRecommend(){
        try(Page<Object> page1 = PageHelper.startPage(1, 3)) {
            List<BlogIdAndTitle> blogIdAndTitles = blogService.getIdAndTitleListByIsPublishedAndIsRecommend();
            return blogIdAndTitles;
        }catch (Exception e){
            e.printStackTrace();
        }
        return List.of();
    }

    public Map<String, Object> getArchiveBlogListIsPublished(){
        List<String> groupYearMonth = blogService.getGroupYearMonthAndIsPublished();
        List<ArchiveBlog> archiveBlogsBatch = blogService.getArchiveBlogListByYearMonthAndIsPublished(groupYearMonth);
        Map<String, List<ArchiveBlogResponse>> blogMap = new LinkedHashMap<>();
        for(int i = archiveBlogsBatch.size() -1 ; i>=0 ;i--){
            ArchiveBlog a = archiveBlogsBatch.get(i);
            blogMap.computeIfAbsent(a.getYM(), k -> new ArrayList<>())
                    .add(blogMapper.toArchiveBlogResponse(a));
        }
        return Map.of(
                "blogMap",blogMap,
                "count", archiveBlogsBatch.size()
        );
    }

    public BlogDetail getBlogByIdAndIsPublished(Long id){
        BlogDetail blogDetail =  blogService.getBlogByIdAndIsPublished(id);
        if(blogDetail.getMusicId() != null){
            try {
                blogDetail.setMusicInfo(musicService.getCompleteSongData(blogDetail.getMusicId(),2));
            } catch (Exception e){
                log.error("Error load music info for songId={}", blogDetail.getMusicId(), e);
                blogDetail.setMusicInfo(null);
            }
        }
        return blogDetail;
    }

    @Async
    public void updateViewByBlogId(Long id){
        blogService.updateViewByBlogId(id);
    }

}


