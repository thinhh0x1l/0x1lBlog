package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Thực thể cơ sở cung cấp các trường chung (id, thời gian) và hành vi xóa mềm. */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    Long id;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;

    OffsetDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
    }

    public void markCreated() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void markUpdated() {
        this.updatedAt = OffsetDateTime.now();
    }
}
