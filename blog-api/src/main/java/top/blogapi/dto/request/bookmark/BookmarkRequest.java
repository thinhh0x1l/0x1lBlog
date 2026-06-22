package top.blogapi.dto.request.bookmark;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookmarkRequest {

    @NotNull(message = "Blog ID is required")
    private Long blogId;

    private String collection;

    private String note;

    private Boolean isPublic;
}
