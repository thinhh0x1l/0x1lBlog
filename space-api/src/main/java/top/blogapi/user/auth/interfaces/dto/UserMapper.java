package top.blogapi.user.auth.interfaces.dto;

import org.mapstruct.Mapper;
import top.blogapi.user.core.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
