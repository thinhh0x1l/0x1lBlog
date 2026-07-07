package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.blogapi.dto.response.StatusResponse;
import top.blogapi.model.entity.Status;

/**
 * Mapper MapStruct để chuyển đổi entity Status sang StatusResponse DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface StatusMapper {

    @Mapping(target = "poll", ignore = true)
    StatusResponse toResponse(Status status);
}
