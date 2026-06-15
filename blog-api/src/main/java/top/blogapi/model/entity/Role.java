package top.blogapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    Long id;
    String name;
    String description;
    LocalDateTime createdAt;
    List<Permission> permissions;
}
