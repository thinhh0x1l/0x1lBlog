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
    public List<Blog> getListByTitleOrType(String query, Integer typeId) {
        return blogMapper.getListByTitleOrType(query, typeId);
    }

    @Override
    public void deleteBlogById(Long id) {
        blogMapper.deleteBlogById(id);
    }
}
