package top.blogapi.dto.request.follow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request body để theo dõi một người dùng.
 */
@Data
public class FollowRequest {

    @NotNull(message = "Following ID is required")
    private Long followingId;
}
