package top.blogapi.dto.request.blog;

import lombok.Data;

import java.util.List;

@Data
public class CreateBlogRequest {
    private String title;
    private String content;
    private String description;
    private String coverImage;
    private Long categoryId;
    private String contentType;
    private String locationName;
    private Boolean allowComments;
    private List<String> hashtags;
}
