package top.blogapi.content.blog.domain.repository;

import top.blogapi.content.blog.domain.entity.BlogHashtag;

import java.util.List;

public interface BlogHashtagRepository {
    void save(BlogHashtag blogHashtag);
    void saveBatch(List<BlogHashtag> blogHashtags);
    void delete(Long blogId, Long hashtagId);
    void deleteByBlogId(Long blogId);
}
