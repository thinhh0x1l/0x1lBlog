package top.blogapi.content.series.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.series.domain.entity.SeriesBlog;

import java.util.List;

@Repository
public interface SeriesBlogJpaRepository extends JpaRepository<SeriesBlog, Long> {

    List<SeriesBlog> findBySeriesIdOrderBySortOrder(Long seriesId);

    @Modifying
    @Query(value = "DELETE FROM series_blogs WHERE series_id = :seriesId AND blog_id = :blogId", nativeQuery = true)
    void deleteBySeriesIdAndBlogId(@Param("seriesId") Long seriesId, @Param("blogId") Long blogId);

    @Modifying
    @Query(value = "DELETE FROM series_blogs WHERE series_id = :seriesId", nativeQuery = true)
    void deleteBySeriesId(@Param("seriesId") Long seriesId);
}
