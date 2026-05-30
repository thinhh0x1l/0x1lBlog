package top.blogapi.dto.request.blog;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class BlogReq {

    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Số từ không được để trống")
    @Min(value = 0, message = "Số từ phải >= 0")
    private Integer words;

    private Integer readTime;

    private Integer views;

    /**
     * Có thể là:
     * - Integer: id category
     * - String: tên category mới
     */
    @NotNull(message = "Thể loại không được để trống")
    private Object cate;

    /**
     * Có thể chứa:
     * - Integer: id tag
     * - String: tên tag mới
     */
    private List<Object> tagList;
}