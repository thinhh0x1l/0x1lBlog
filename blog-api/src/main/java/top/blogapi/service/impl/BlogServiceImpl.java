package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.entity.Blog;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.repository.BlogRepository;
import top.blogapi.service.BlogService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BlogServiceImpl implements BlogService {
    BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public List<Blog> getListByTitleOrCategory(String query, Integer categoryId) {
        return blogRepository.getListByTitleOrCategory(query, categoryId);
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
    public int saveBlogTag(Long blogId, Long tagId) {
        return blogRepository.saveBlogTag(blogId, tagId);
    }

}
