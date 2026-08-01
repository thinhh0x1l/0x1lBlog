package top.blogapi.content.category.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;
import top.blogapi.shared.BaseEntity;

@Entity
@Table(name = "categories")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {
    @Column(nullable = false, length = 50)
    String name;

    @Column(nullable = false, length = 100)
    String slug;

    @Column(length = 500)
    String description;

    @Column(length = 50)
    String icon;

    @Column(length = 20)
    String color;

    Integer sortOrder;

    Integer blogCount;

    Boolean isVisible;
}
