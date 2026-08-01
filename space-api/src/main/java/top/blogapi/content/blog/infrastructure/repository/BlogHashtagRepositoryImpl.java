package top.blogapi.content.blog.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.content.blog.domain.entity.BlogHashtag;
import top.blogapi.content.blog.domain.repository.BlogHashtagRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BlogHashtagRepositoryImpl implements BlogHashtagRepository {

    private final BlogHashtagJpaRepository jpa;

    @Override
    public void save(BlogHashtag blogHashtag) {
        jpa.save(blogHashtag);
    }

    @Override
    public void saveBatch(List<BlogHashtag> blogHashtags) {
        jpa.saveAll(blogHashtags);
    }

    @Override
    public void delete(Long blogId, Long hashtagId) {
        jpa.deleteByBlogIdAndHashtagId(blogId, hashtagId);
    }

    @Override
    public void deleteByBlogId(Long blogId) {
        jpa.deleteByBlogId(blogId);
    }
}
