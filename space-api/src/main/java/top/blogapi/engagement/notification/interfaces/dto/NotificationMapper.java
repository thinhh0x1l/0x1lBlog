package top.blogapi.engagement.notification.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.engagement.notification.domain.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
}
