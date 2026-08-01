package top.blogapi.social.playlist.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PlaylistVoteMybatisMapper {

    @Select("SELECT COALESCE(SUM(vote), 0) FROM playlist_votes WHERE song_id = #{songId}")
    int sumVotesBySongId(@Param("songId") Long songId);
}
