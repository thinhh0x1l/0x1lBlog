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
@Table(name = "canvas_strokes")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CanvasStroke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "canvas_id", nullable = false)
    Long canvasId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    int x;

    int y;

    @Column(nullable = false, length = 20)
    String color;

    int brushSize;

    Instant createdAt;
}
