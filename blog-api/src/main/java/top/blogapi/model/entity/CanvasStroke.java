package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Một nét vẽ đơn lẻ do người dùng đặt trên canvas. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CanvasStroke {
    Long id;
    Long canvasId;
    Long userId;
    int x;
    int y;
    String color;
    int brushSize;
    OffsetDateTime createdAt;
}
