package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.response.PlaylistResponse;
import top.blogapi.dto.response.PlaylistSongResponse;
import top.blogapi.model.entity.Playlist;
import top.blogapi.model.entity.PlaylistSong;

/**
 * Mapper MapStruct để chuyển đổi entity Playlist và PlaylistSong sang DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PlaylistMapper {

    @Mapping(target = "songs", ignore = true)
    PlaylistResponse toResponse(Playlist playlist);

    PlaylistSongResponse toSongResponse(PlaylistSong song);
}
