package top.blogapi.model.event;

import lombok.Value;
import top.blogapi.model.entity.Status;

/** Sự kiện được kích hoạt khi cập nhật trạng thái được tạo. */
@Value
public class StatusCreatedEvent {
    Status status;
}
