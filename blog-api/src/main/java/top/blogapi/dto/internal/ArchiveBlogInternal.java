package top.blogapi.dto.internal;

import lombok.Value;

/**
 * DTO nội bộ cho các mục lưu trữ blog được nhóm theo năm và tháng.
 */
@Value
public class ArchiveBlogInternal {
    Integer year;
    Integer month;
    Long blogId;
    String title;
    String slug;
}
