package top.blogapi.dto.request.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationRequest {
    @Min(0)
    Integer pageNum = 1;
    @Max(100)
    @Min(1)
    Integer pageSize = 10;
    String sortBy = "create_time";
    String sortOrder = "DESC";
}
