package top.blogapi.social.status.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.social.status.domain.entity.Status;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusResponse toResponse(Status status);
}
