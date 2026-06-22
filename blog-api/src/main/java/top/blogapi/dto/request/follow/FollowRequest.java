package top.blogapi.dto.request.follow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowRequest {

    @NotNull(message = "Following ID is required")
    private Long followingId;
}
