package top.blogapi.dto.request.blog;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.request.common.PaginationRequest;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BlogQueryRequest {
    String query = "" ;
    Integer categoryId = null ;
    @Min(0)
    Integer pageNum = 1;
    @Max(100)
    @Min(1)
    Integer pageSize = 10;
    String sortBy = "create_time";
    String sortOrder = "DESC";


}
