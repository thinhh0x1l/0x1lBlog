package top.blogapi.content.series.domain.entity;

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
@Table(name = "blog_series")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogSeries extends BaseEntity {
    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 500)
    String description;

    @Column(length = 500)
    String coverImage;

    @Column(name = "author_id", nullable = false)
    Long authorId;

    @Column(length = 20)
    String status;

    Integer price;

    Integer postCount;

    Integer subscriberCount;
}
