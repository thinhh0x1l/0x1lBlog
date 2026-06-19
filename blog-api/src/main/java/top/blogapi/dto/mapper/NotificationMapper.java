package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import top.blogapi.dto.response.NotificationResponse;
import top.blogapi.model.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}
