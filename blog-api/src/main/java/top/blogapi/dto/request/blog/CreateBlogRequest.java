package top.blogapi.dto.request.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateBlogRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 100000, message = "Content must not exceed 100000 characters")
    private String content;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
    private String coverImage;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Content type is required")
    @Size(max = 50, message = "Content type must not exceed 50 characters")
    private String contentType;

    @Size(max = 200, message = "Location name must not exceed 200 characters")
    private String locationName;

    private Boolean allowComments;
    private List<String> hashtags;
}
