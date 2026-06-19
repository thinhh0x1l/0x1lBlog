package top.blogapi.dto.internal;

import lombok.Value;

@Value
public class ArchiveBlogInternal {
    Integer year;
    Integer month;
    Long blogId;
    String title;
    String slug;
}
