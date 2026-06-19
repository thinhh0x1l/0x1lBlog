package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.SeriesSubscriber;

import java.util.Optional;

@Mapper
public interface SeriesSubscriberRepository {

    @Select("SELECT * FROM series_subscribers WHERE series_id = #{seriesId} AND user_id = #{userId}")
    Optional<SeriesSubscriber> findBySeriesAndUser(Long seriesId, Long userId);

    @Insert("INSERT INTO series_subscribers (series_id, user_id) VALUES (#{seriesId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SeriesSubscriber subscriber);

    @Delete("DELETE FROM series_subscribers WHERE series_id = #{seriesId} AND user_id = #{userId}")
    int delete(Long seriesId, Long userId);

    @Select("SELECT COUNT(*) FROM series_subscribers WHERE series_id = #{seriesId}")
    long countBySeriesId(Long seriesId);

    @Select("SELECT EXISTS(SELECT 1 FROM series_subscribers WHERE series_id = #{seriesId} AND user_id = #{userId})")
    boolean exists(Long seriesId, Long userId);
}
