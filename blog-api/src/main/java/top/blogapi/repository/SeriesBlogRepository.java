package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.SeriesBlog;

import java.util.List;

/**
 * MyBatis mapper cho bảng liên kết {@code series_blogs}. Quản lý quan hệ
 * nhiều-nhiều có thứ tự giữa series và blog.
 */
@Mapper
public interface SeriesBlogRepository {

    @Select("SELECT * FROM series_blogs WHERE series_id = #{seriesId} ORDER BY sort_order")
    List<SeriesBlog> findBySeriesId(Long seriesId);

    @Insert("INSERT INTO series_blogs (series_id, blog_id, sort_order, note) VALUES (#{seriesId}, #{blogId}, #{sortOrder}, #{note})")
    int insert(SeriesBlog seriesBlog);

    @Delete("DELETE FROM series_blogs WHERE series_id = #{seriesId} AND blog_id = #{blogId}")
    int delete(Long seriesId, Long blogId);

    @Delete("DELETE FROM series_blogs WHERE series_id = #{seriesId}")
    int deleteBySeriesId(Long seriesId);
}
