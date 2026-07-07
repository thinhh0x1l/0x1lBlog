package top.blogapi.dto.mapper;

import org.mapstruct.Mapper;
import top.blogapi.dto.response.UserResponse;
import top.blogapi.model.entity.User;

/**
 * Mapper MapStruct để chuyển đổi entity User sang UserResponse DTO.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
