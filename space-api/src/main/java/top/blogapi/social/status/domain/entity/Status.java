package top.blogapi.social.status.domain.entity;

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
@Table(name = "statuses")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Status extends BaseEntity {
    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "thread_id")
    Long threadId;

    Integer partOrder;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(length = 500)
    String imageUrl;

    @Column(length = 20)
    String visibility;
}
