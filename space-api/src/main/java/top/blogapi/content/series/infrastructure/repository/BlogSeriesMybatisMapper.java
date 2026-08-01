package top.blogapi.content.series.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BlogSeriesMybatisMapper {

    @Update("UPDATE blog_series SET post_count = (SELECT COUNT(*) FROM series_blogs WHERE series_id = #{seriesId}) WHERE id = #{seriesId}")
    void refreshPostCount(@Param("seriesId") Long seriesId);

    @Update("UPDATE blog_series SET subscriber_count = (SELECT COUNT(*) FROM series_subscribers WHERE series_id = #{seriesId}) WHERE id = #{seriesId}")
    void refreshSubscriberCount(@Param("seriesId") Long seriesId);
}
