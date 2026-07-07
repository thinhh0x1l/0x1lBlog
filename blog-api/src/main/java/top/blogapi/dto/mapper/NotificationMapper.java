package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import top.blogapi.dto.response.NotificationResponse;
import top.blogapi.model.entity.Notification;

/**
 * Mapper MapStruct để chuyển đổi entity Notification sang NotificationResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}
