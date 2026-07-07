package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** Danh mục phân loại bài viết blog theo chủ đề. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {
    String name;
    String slug;
    String description;
    String icon;
    String color;
    Integer sortOrder;
    Integer blogCount;
    Boolean isVisible;
}
