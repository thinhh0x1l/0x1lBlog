package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Vật phẩm trong túi đồ người dùng, có thể có thời hạn. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInventory {
    Long id;
    Long userId;
    Long itemId;
    Integer serialNumber;
    String source;
    OffsetDateTime acquiredAt;
    OffsetDateTime expiresAt;
    Boolean isEquipped;
    Integer tradeCount;
}
