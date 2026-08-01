package top.blogapi.content.blog.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;
import top.blogapi.shared.BaseEntity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "blogs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"author_id", "slug"})
})
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog extends BaseEntity {
    @Column(name = "author_id", nullable = false)
    Long authorId;

    @Column(name = "category_id")
    Long categoryId;

    @Column(length = 100)
    String authorName;

    @Column(length = 500)
    String authorAvatar;

    @Column(length = 100)
    String categoryName;

    @Column(nullable = false, length = 200)
    String title;

    @Column(nullable = false, length = 255)
    String slug;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(length = 500)
    String description;

    @Column(length = 500)
    String coverImage;

    @Column(length = 50)
    String contentType;

    @Column(length = 200)
    String locationName;

    BigDecimal latitude;

    BigDecimal longitude;

    @Column(length = 20)
    String status;

    @Column(length = 20)
    String visibility;

    Integer price;

    Boolean isTop;

    Boolean isRecommend;

    Boolean allowComments;

    Integer words;

    Integer readTime;

    Integer views;

    Integer likeCount;

    Integer loveCount;

    Integer hahaCount;

    Integer wowCount;

    Integer sadCount;

    Integer angryCount;

    Integer commentCount;

    Integer bookmarkCount;

    Integer shareCount;

    Instant publishedAt;

    Instant lastCommentedAt;
}
