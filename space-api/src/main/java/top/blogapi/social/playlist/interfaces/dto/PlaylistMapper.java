package top.blogapi.social.playlist.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.social.playlist.domain.entity.Playlist;
import top.blogapi.social.playlist.domain.entity.PlaylistSong;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    PlaylistResponse toResponse(Playlist playlist);
    PlaylistSongResponse toSongResponse(PlaylistSong song);
}
