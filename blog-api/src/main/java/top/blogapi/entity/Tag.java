package top.blogapi.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    Long id;
    String name;
    String color; //Màu tag (tương ứng với màu do Semantic UI cung cấp, tùy chọn)
    List<Blog> blogs = new ArrayList<>(); //Các bài đăng trên blog có tag này
}
