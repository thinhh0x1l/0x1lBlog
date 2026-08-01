package top.blogapi.content.series.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.series.domain.entity.BlogSeries;

import java.util.List;

@Repository
public interface BlogSeriesJpaRepository extends JpaRepository<BlogSeries, Long> {

    List<BlogSeries> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    @Modifying
    @Query(value = "UPDATE blog_series SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDelete(@Param("id") Long id);
}
