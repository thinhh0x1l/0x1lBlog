package top.blogapi.dto.request;

import lombok.Data;

@Data
public class PaginationRequest {
    private int page = 0;
    private int size = 10;
}
