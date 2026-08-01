package top.blogapi.social.canvas.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "canvases")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Canvas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 50)
    String type;

    @Column(nullable = false, length = 200)
    String title;

    int width;

    int height;

    @Column(name = "owner_id", nullable = false)
    Long ownerId;

    Instant startsAt;

    Instant endsAt;

    @Column(nullable = false)
    Boolean isActive;

    Instant createdAt;
}
