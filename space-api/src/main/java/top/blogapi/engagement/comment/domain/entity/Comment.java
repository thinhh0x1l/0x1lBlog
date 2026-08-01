package top.blogapi.engagement.comment.domain.entity;

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
@Table(name = "comments")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Column(name = "target_type", nullable = false, length = 20)
    String targetType;

    @Column(name = "target_id", nullable = false)
    Long targetId;

    @Column(name = "parent_id")
    Long parentId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(length = 100)
    String authorName;

    @Column(length = 500)
    String authorAvatar;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(length = 20)
    String status;

    Integer replyCount;
}
