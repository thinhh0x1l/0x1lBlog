package top.blogapi.dto.response.common;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationResponse {
    @Min(0)
    Integer pageNum;
    @Max(100)
    @Min(1)
    Integer pageSize;
    String sortBy = "create_time";
    String sortOrder = "DESC";

}
