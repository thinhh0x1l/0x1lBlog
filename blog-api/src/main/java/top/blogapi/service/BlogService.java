package top.blogapi.service;

import top.blogapi.entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getListByTitleOrType(String query, Integer typeId);

    void deleteBlogById(Long id);
}
