package top.blogapi.dto.request.blog;

import lombok.Data;

@Data
public class UpdateBlogRequest {
    private String title;
    private String content;
    private String description;
    private String coverImage;
    private Long categoryId;
    private String contentType;
    private Boolean allowComments;
}
