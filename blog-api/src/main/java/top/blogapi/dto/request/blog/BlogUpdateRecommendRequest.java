package top.blogapi.dto.request.blog;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BlogUpdateRecommendRequest {
    @NotNull
    Long id;
    @NotNull
    boolean recommend;
}
