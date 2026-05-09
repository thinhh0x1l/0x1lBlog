package top.blogapi.model.vo;


import com.github.pagehelper.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResult<T> {
    Integer pageNum;
    Integer pageSize;
    Integer totalPages;
    Long totalElements;
    List<T> items;

    public static <T> PageResult<T> from(PageInfo<T> pageInfo) {

        return new PageResult<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );
    }
}