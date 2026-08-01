package top.blogapi.admin.domain.repository;

import java.util.List;
import java.util.Optional;

public interface AdminBlogRepository {
    Optional<Object[]> findById(Long id);
    List<Object[]> findPublished(int page, int size);
    long countPublished();
    void toggleTop(Long id, boolean isTop);
    void toggleRecommend(Long id, boolean isRecommend);
    void softDelete(Long id);
}
