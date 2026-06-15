package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.SeriesSubscriber;

import java.util.Optional;

@Repository
@Mapper
public interface SeriesSubscriberRepository {

    @Insert("INSERT INTO series_subscriber (series_id, user_id, created_at) VALUES (#{seriesId}, #{userId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(SeriesSubscriber subscriber);

    @Delete("DELETE FROM series_subscriber WHERE series_id = #{seriesId} AND user_id = #{userId}")
    int delete(@Param("seriesId") Long seriesId, @Param("userId") Long userId);

    @Select("SELECT * FROM series_subscriber WHERE series_id = #{seriesId} AND user_id = #{userId} LIMIT 1")
    Optional<SeriesSubscriber> findBySeriesAndUser(@Param("seriesId") Long seriesId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM series_subscriber WHERE series_id = #{seriesId}")
    int countBySeriesId(@Param("seriesId") Long seriesId);

    @Select("SELECT COUNT(*) > 0 FROM series_subscriber WHERE series_id = #{seriesId} AND user_id = #{userId}")
    boolean exists(@Param("seriesId") Long seriesId, @Param("userId") Long userId);
}
