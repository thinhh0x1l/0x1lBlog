package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.mapper.BlogMapper;
import top.blogapi.dto.request.blog.CreateBlogRequest;
import top.blogapi.dto.request.blog.UpdateBlogRequest;
import top.blogapi.dto.response.BlogResponse;
import top.blogapi.model.entity.Blog;
import top.blogapi.model.entity.BlogHashtag;
import top.blogapi.model.entity.Hashtag;
import top.blogapi.model.event.BlogPublishedEvent;
import top.blogapi.repository.BlogHashtagRepository;
import top.blogapi.service.CacheService;
import top.blogapi.service.blog.BlogService;
import top.blogapi.service.hashtag.HashtagService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogOrchestrator {

    private final BlogService blogService;
    private final HashtagService hashtagService;
    private final BlogHashtagRepository blogHashtagRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BlogMapper blogMapper;
    private final CacheService cacheService;

    @Transactional
    public BlogResponse createBlog(CreateBlogRequest request, Long authorId) {
        Blog blog = blogMapper.toEntity(request);
        blog.setAuthorId(authorId);
        blog.setStatus("DRAFT");
        blog.setVisibility("PUBLIC");
        blogService.create(blog);

        if (request.getHashtags() != null) {
            for (String tagName : request.getHashtags()) {
                Hashtag tag = hashtagService.findOrCreate(tagName);
                BlogHashtag bh = new BlogHashtag();
                bh.setBlogId(blog.getId());
                bh.setHashtagId(tag.getId());
                blogHashtagRepository.insert(bh);
            }
        }

        BlogResponse response = blogMapper.toResponse(blog);
        enrichBlogResponse(response, blog);
        return response;
    }

    @Transactional
    public BlogResponse updateBlog(Long id, UpdateBlogRequest request) {
        Blog blog = blogService.findById(id);
        if (request.getTitle() != null) blog.setTitle(request.getTitle());
        if (request.getContent() != null) blog.setContent(request.getContent());
        if (request.getDescription() != null) blog.setDescription(request.getDescription());
        if (request.getCoverImage() != null) blog.setCoverImage(request.getCoverImage());
        if (request.getCategoryId() != null) blog.setCategoryId(request.getCategoryId());
        if (request.getContentType() != null) blog.setContentType(request.getContentType());
        if (request.getAllowComments() != null) blog.setAllowComments(request.getAllowComments());
        blog = blogService.update(blog);

        cacheService.evictBlog(id);
        BlogResponse response = blogMapper.toResponse(blog);
        enrichBlogResponse(response, blog);
        return response;
    }

    @Transactional
    public BlogResponse publishBlog(Long id) {
        blogService.publish(id);
        Blog blog = blogService.findById(id);
        eventPublisher.publishEvent(new BlogPublishedEvent(blog));

        cacheService.evictBlog(id);
        BlogResponse response = blogMapper.toResponse(blog);
        enrichBlogResponse(response, blog);
        return response;
    }

    @Transactional
    public void deleteBlog(Long id) {
        blogService.softDelete(id);
        cacheService.evictBlog(id);
    }

    public BlogResponse getBlog(Long id) {
        Blog blog = blogService.findById(id);
        BlogResponse response = blogMapper.toResponse(blog);
        enrichBlogResponse(response, blog);
        return response;
    }

    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogService.findBySlug(slug);
        BlogResponse response = blogMapper.toResponse(blog);
        enrichBlogResponse(response, blog);
        return response;
    }

    private void enrichBlogResponse(BlogResponse response, Blog blog) {
        response.setAuthorName("author_" + blog.getAuthorId());
    }
}
