package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.blogapi.entity.Blog;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.service.BlogService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BlogServiceImpl implements BlogService {
    BlogMapper blogMapper;
    @Override
    public List<Blog> getBlogList() {
        return blogMapper.getBlogList();
    }
}
